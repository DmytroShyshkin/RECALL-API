package com.dmytro.language_learning_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.dmytro.language_learning_api.exception.EnrichmentException.EnrichmentServiceRateLimitedException;
import com.dmytro.language_learning_api.exception.EnrichmentException.EnrichmentServiceTimeoutException;
import com.dmytro.language_learning_api.exception.EnrichmentException.EnrichmentServiceUnavailableException;
import com.dmytro.language_learning_api.kafka.producer.word.WordUpsertedProducer;
import com.dmytro.language_learning_api.mapper.TranslationMapper;
import com.dmytro.language_learning_api.mapper.WordUpsertedEventMapper;
import com.dmytro.language_learning_api.mapper.WordsMapper;
import com.dmytro.language_learning_api.model.Translation;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.model.Words;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.WordsRepository;
import com.dmytro.language_learning_api.repository.statistics.WordStatisticsRepository;
import com.dmytro.language_learning_api.security.jwt.JwtUtil;

/**
 * Scoped to the error-mapping logic added around the call to the Python
 * enrichment service in enrichWord(): HTTP 429/503 via RestClient's
 * onStatus(), and the two ResourceAccessException cases (socket timeout
 * vs. everything else) via the surrounding catch block. The rest of
 * enrichWord() (not-found, ownership check, picking/creating the
 * Translation row) is intentionally not covered here.
 *
 * Uses MockRestServiceServer bound to a real RestClient.Builder, so the
 * actual onStatus()/retrieve() logic in WordsServiceImpl runs for real
 * against a simulated HTTP response instead of a hand-mocked RestClient
 * fluent-API chain. See https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-client.html
 */
@ExtendWith(MockitoExtension.class)
class WordsServiceImplEnrichWordTest {

    @Mock private WordsMapper wordsMapper;
    @Mock private TranslationMapper translationMapper;
    @Mock private WordUpsertedEventMapper wordUpsertedEventMapper;
    @Mock private WordsRepository wordsRepository;
    @Mock private UsersRepository usersRepository;
    @Mock private WordStatisticsRepository wordStatisticsRepository;
    @Mock private WordUpsertedProducer wordUpsertedProducer;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private JwtUtil jwtUtil;

    private MockRestServiceServer mockServer;
    private WordsServiceImpl service;

    private UUID wordId;
    private Translation translation;

    @BeforeEach
    void setUp() {
        // Bind the mock server to the builder BEFORE build() -- binding is
        // what installs the stub ClientHttpRequestFactory on the builder.
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8000");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient enrichmentServiceRestClient = builder.build();

        service = new WordsServiceImpl(
                wordsMapper,
                translationMapper,
                wordUpsertedEventMapper,
                wordsRepository,
                usersRepository,
                wordStatisticsRepository,
                enrichmentServiceRestClient,
                wordUpsertedProducer,
                eventPublisher,
                jwtUtil
        );

        Users owner = new Users();
        owner.setId(UUID.randomUUID());

        wordId = UUID.randomUUID();
        Words word = new Words();
        word.setId(wordId);
        word.setOriginalWord("hello");
        word.setSourceLanguage("en");
        word.setOwner(owner);

        translation = new Translation();
        translation.setTargetLanguage("es");
        translation.setWord(word);
        word.setTranslations(new ArrayList<>(List.of(translation)));

        when(wordsRepository.findById(wordId)).thenReturn(Optional.of(word));
        when(jwtUtil.getCurrentUser()).thenReturn(owner);
    }

    @Test
    void enrichWord_onSuccess_appliesTranslationAndDescription() {
        mockServer.expect(requestTo("http://localhost:8000/enrich"))
                .andRespond(withSuccess(
                        """
                        {"translation": "hola", "description": "greeting"}
                        """,
                        MediaType.APPLICATION_JSON
                ));

        service.enrichWord(wordId, "es");

        assertThat(translation.getTranslatedWord()).isEqualTo("hola");
        assertThat(translation.getDescription()).isEqualTo("greeting");
        mockServer.verify();
    }

    @Test
    void enrichWord_onHttp429_throwsRateLimitedAndDoesNotPersist() {
        mockServer.expect(requestTo("http://localhost:8000/enrich"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThatThrownBy(() -> service.enrichWord(wordId, "es"))
                .isInstanceOf(EnrichmentServiceRateLimitedException.class);

        verifyNoInteractions(wordUpsertedProducer);
    }

    @Test
    void enrichWord_onHttp503_throwsUnavailableAndDoesNotPersist() {
        mockServer.expect(requestTo("http://localhost:8000/enrich"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        assertThatThrownBy(() -> service.enrichWord(wordId, "es"))
                .isInstanceOf(EnrichmentServiceUnavailableException.class);

        verifyNoInteractions(wordUpsertedProducer);
    }

    @Test
    void enrichWord_onSocketTimeout_throwsTimeoutException() {
        // No HTTP response at all: RestClient wraps any IOException thrown
        // while executing the request into ResourceAccessException -- the
        // exact case the catch block in enrichWord() has to tell apart.
        mockServer.expect(requestTo("http://localhost:8000/enrich"))
                .andRespond(request -> {
                    throw new SocketTimeoutException("Read timed out");
                });

        assertThatThrownBy(() -> service.enrichWord(wordId, "es"))
                .isInstanceOf(EnrichmentServiceTimeoutException.class);

        verifyNoInteractions(wordUpsertedProducer);
    }

    @Test
    void enrichWord_onConnectionRefused_throwsUnavailableException() {
        mockServer.expect(requestTo("http://localhost:8000/enrich"))
                .andRespond(request -> {
                    throw new ConnectException("Connection refused");
                });

        assertThatThrownBy(() -> service.enrichWord(wordId, "es"))
                .isInstanceOf(EnrichmentServiceUnavailableException.class);

        verifyNoInteractions(wordUpsertedProducer);
    }
}

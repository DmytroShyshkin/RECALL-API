package com.dmytro.language_learning_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.dto.response.PageResponse;
import com.dmytro.language_learning_api.exception.NotFoundException.TranslationNotFoundException;
import com.dmytro.language_learning_api.exception.NotFoundException.WordNotFoundException;
import com.dmytro.language_learning_api.kafka.producer.word.WordUpsertedProducer;
import com.dmytro.language_learning_api.mapper.TranslationMapper;
import com.dmytro.language_learning_api.mapper.WordUpsertedEventMapper;
import com.dmytro.language_learning_api.model.Translation;
import com.dmytro.language_learning_api.model.Words;
import com.dmytro.language_learning_api.repository.TranslationRepository;
import com.dmytro.language_learning_api.repository.WordsRepository;
import com.dmytro.language_learning_api.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationMapper translationMapper;
    private final WordsRepository wordsRepository;
    private final WordUpsertedProducer wordUpsertedProducer;
    private final WordUpsertedEventMapper wordUpsertedEventMapper;
    private final JwtUtil jwtUtil;

    @Override
    public PageResponse<TranslationDTO> getTranslationsByWordId(UUID wordId, int pageNo, int pageSize) {
        UUID ownerId = jwtUtil.getCurrentUser().getId();
        
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Translation> translationsPage =
                translationRepository.findByWordIdAndWordOwnerId(wordId, ownerId, pageable);
        List<Translation> translation = translationsPage.getContent();
        if (translation.isEmpty()) {
            throw new TranslationNotFoundException("Translations not found for word id: " + wordId);
        }
        List<TranslationDTO> translationsList = translation.stream().
                map(translationMapper::toDto).toList();

        PageResponse<TranslationDTO> translationRespons = new PageResponse<TranslationDTO>();
        translationRespons.setContent(translationsList);
        translationRespons.setPageNo(translationsPage.getNumber());
        translationRespons.setPageSize(translationsPage.getSize());
        translationRespons.setTotalPages(translationsPage.getTotalPages());
        translationRespons.setTotalElements(translationsPage.getTotalElements());
        translationRespons.setLast(translationsPage.isLast());
        return translationRespons;
    }

    @Override
    public TranslationDTO addTranslation(UUID wordId, TranslationDTO translationDto) {
        UUID ownerId = jwtUtil.getCurrentUser().getId();

        Words word = wordsRepository.findByIdAndOwnerId(wordId, ownerId)
                .orElseThrow(() -> new WordNotFoundException("Word not found"));

        Translation translation = translationMapper.fromDto(translationDto);
        translation.setWord(word);

        translationRepository.save(translation);
        publishWordUpserted(wordId);

        return translationMapper.toDto(translation);
    }

    @Override
    public TranslationDTO getTranslationById(UUID translationId) {
        Translation translation = getTranslationOrThrow(translationId);
        return translationMapper.toDto(translation);
    }

    @Override
    public TranslationDTO updateTranslation(UUID translationId, TranslationDTO translationDto) {
        Translation translation = getTranslationOrThrow(translationId);

        translation.setTranslatedWord(translationDto.translatedWord());
        translation.setTargetLanguage(translationDto.targetLanguage());

        translationRepository.save(translation);
        publishWordUpserted(translation.getWord().getId());

        return translationMapper.toDto(translation);
    }

    @Override
    public void deleteTranslation(UUID translationId) {
        Translation translation = getTranslationOrThrow(translationId);
        UUID wordId = translation.getWord().getId();

        translationRepository.delete(translation);
        publishWordUpserted(wordId);
    }

    // Clases auxiliares
    private Translation getTranslationOrThrow(UUID translationId) {
        UUID ownerId = jwtUtil.getCurrentUser().getId();

        return translationRepository.findByIdAndWordOwnerId(translationId, ownerId)
                .orElseThrow(() -> new TranslationNotFoundException("Translationwith id " + translationId + " not found"));
    }

    private void publishWordUpserted(UUID wordId) {
        wordsRepository.findByIdWithTranslations(wordId)
                .ifPresent(word -> wordUpsertedProducer.sendWordUpsertedEvent(wordUpsertedEventMapper.toEvent(word)));
    }
}
package com.dmytro.language_learning_api.service;

import com.dmytro.language_learning_api.dto.requests.createRequests.CreateWordRequestDTO;
import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateWordRequest;
import com.dmytro.language_learning_api.dto.WordsDTO;
import com.dmytro.language_learning_api.dto.response.PageResponse;
import com.dmytro.language_learning_api.exception.NotFoundException.UserNotFoundException;
import com.dmytro.language_learning_api.exception.NotFoundException.WordNotFoundException;
import com.dmytro.language_learning_api.mapper.TranslationMapper;
import com.dmytro.language_learning_api.mapper.WordsMapper;
import com.dmytro.language_learning_api.model.Translation;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.model.Words;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.WordsRepository;
import com.dmytro.language_learning_api.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordsServiceImpl implements WordsService {

    // Mappers
    private final WordsMapper wordsMapper;
    private final TranslationMapper translationMapper;
    // Repository
    private final WordsRepository wordsRepository;
    private final UsersRepository usersRepository;

    // Security helper
    private final JwtUtil jwtUtil;

    @Override
    public WordsDTO createWord(CreateWordRequestDTO request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Users user = usersRepository.findByEmail(email)
                .orElseThrow();

        Words word = new Words();
        word.setOriginalWord(request.originalWord());
        word.setSourceLanguage(request.sourceLanguage());
        word.setOwner(user);

        return wordsMapper.toDto(wordsRepository.save(word));
    }

    @Transactional(readOnly = true)
    @Override
    public WordsDTO getWordById(UUID wordId) {
        Words word = getWordOrThrow(wordId);
        //return wordsMapper.toDto(word);

        Set<UUID> synonymIds = word.getSynonyms() == null
                ? Collections.emptySet()
                : word.getSynonyms().stream()
                .map(Words::getId)
                .collect(Collectors.toSet());

        return wordsMapper.toDto(word);
    }

    @Override
    public WordsDTO updateWord(UUID wordId, UpdateWordRequest updateWordRequest) {
        Words word = getWordOrThrow(wordId);

        if (updateWordRequest.sourceLanguage() != null && !updateWordRequest.sourceLanguage().isBlank()
                && !updateWordRequest.sourceLanguage().equals(word.getSourceLanguage())) {
            word.setSourceLanguage(updateWordRequest.sourceLanguage());
        }

        if (updateWordRequest.originalWord() != null && !updateWordRequest.originalWord().isBlank()
                && !updateWordRequest.originalWord().equals(word.getOriginalWord())) {
            word.setOriginalWord(updateWordRequest.originalWord());
        }

        wordsRepository.save(word);

        return wordsMapper.toDto(word);
    }

    @Override
    public PageResponse<WordsDTO> getAllWordsByOwnerEmail(String ownerEmail, int pageNo, int pageSize) {
        Users user = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(
                        ()-> new UserNotFoundException("User by email '" + ownerEmail + "' not found")
                );

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Words> wordsPage = wordsRepository.findByOwnerId(user.getId(), pageable);
        List<Words> words = wordsPage.getContent();
        List<WordsDTO> wordsList = words.stream()
                .map(word -> {

                    WordsDTO dto = wordsMapper.toDto(word);

                    Set<UUID> synonymIds = word.getSynonyms() == null
                            ? Collections.emptySet()
                            : word.getSynonyms().stream()
                            .map(Words::getId)
                            .collect(Collectors.toSet());

                    return new WordsDTO(
                            dto.id(),
                            dto.sourceLanguage(),
                            dto.originalWord(),
                            //dto.ownerId(),
                            synonymIds
                    );

                })
                .toList();

        PageResponse<WordsDTO> wordRespons = new PageResponse<>();
        wordRespons.setContent(wordsList);
        wordRespons.setPageNo(wordsPage.getNumber());
        wordRespons.setPageSize(wordsPage.getSize());
        wordRespons.setTotalPages(wordsPage.getTotalPages());
        wordRespons.setTotalElements(wordsPage.getTotalElements());
        wordRespons.setLast(wordsPage.isLast());

        return wordRespons;
    }

    @Override
    public WordsDTO addTranslationToWord(UUID wordId, TranslationDTO translationDto) {
        Words word = getWordOrThrow(wordId);

        Translation translation = translationMapper.fromDto(translationDto);
        translation.setWord(word);

        word.getTranslations().add(translation);
        Words savedWord = wordsRepository.save(word);

        return wordsMapper.toDto(savedWord);
    }

    @Override
    public void addSynonym(UUID wordId, UUID synonymId) {
        Words word = wordsRepository.findById(wordId)
                .orElseThrow(() -> new WordNotFoundException("Word not found"));

        Words synonym = wordsRepository.findById(synonymId)
                .orElseThrow(() -> new WordNotFoundException("Synonym word not found"));

        Users currentUser = jwtUtil.getCurrentUser();

        if (!word.getOwner().equals(currentUser) ||
                !synonym.getOwner().equals(currentUser)) {
            throw new AccessDeniedException("You don't own these words");
        }

        word.getSynonyms().add(synonym);
        synonym.getSynonyms().add(word);

        wordsRepository.save(word);
        wordsRepository.save(synonym);
    }

    @Override
    public void deleteWord(UUID wordId) {
        Words word = getWordOrThrow(wordId);
        wordsRepository.delete(word);
    }

    // Clases auxiliares
    private Words getWordOrThrow(UUID wordId) {
        return wordsRepository
                .findById(wordId)
                .orElseThrow(() -> new WordNotFoundException(
                        "Word with id " + wordId + " not found"
                ));
    }
}

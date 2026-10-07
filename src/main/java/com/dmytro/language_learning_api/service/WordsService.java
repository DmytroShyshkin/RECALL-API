package com.dmytro.language_learning_api.service;

import java.util.UUID;

import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.dto.WordsDTO;
import com.dmytro.language_learning_api.dto.requests.createRequests.CreateWordRequestDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateWordRequest;
import com.dmytro.language_learning_api.dto.response.PageResponse;

public interface WordsService {

    WordsDTO createWord(CreateWordRequestDTO request);
    WordsDTO getWordById(UUID wordId);
    WordsDTO updateWord(UUID wordId, UpdateWordRequest updateWordRequest);
    void enrichWord(UUID wordId, String targetLanguage);
    PageResponse<WordsDTO> getAllWordsByOwnerEmail(String ownerEmail, int pageNo, int pageSize);
    WordsDTO addTranslationToWord(UUID wordId, TranslationDTO dto);
    void addSynonym(UUID wordId, UUID synonymId);
    void removeSynonym(UUID wordId, UUID synonymId);
    void deleteWord(UUID wordId);
}

package com.dmytro.language_learning_api.service;

import com.dmytro.language_learning_api.dto.requests.createRequests.CreateWordRequestDTO;
import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateWordRequest;
import com.dmytro.language_learning_api.dto.WordsDTO;
import com.dmytro.language_learning_api.dto.response.PageResponse;

import java.util.UUID;

public interface WordsService {

    WordsDTO createWord(CreateWordRequestDTO request);
    WordsDTO getWordById(UUID wordId);
    //List<WordsDTO> getWordsByUser(UUID ownerId);
    WordsDTO updateWord(UUID wordId, UpdateWordRequest updateWordRequest);
    PageResponse<WordsDTO> getAllWordsByOwnerEmail(String ownerEmail, int pageNo, int pageSize);
    WordsDTO addTranslationToWord(UUID wordId, TranslationDTO dto);
    void addSynonym(UUID wordId, UUID synonymId);
    void deleteWord(UUID wordId);
}

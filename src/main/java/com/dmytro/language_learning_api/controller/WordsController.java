package com.dmytro.language_learning_api.controller;

import com.dmytro.language_learning_api.dto.requests.createRequests.CreateWordRequestDTO;
import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateWordRequest;
import com.dmytro.language_learning_api.dto.WordsDTO;
import com.dmytro.language_learning_api.dto.response.PageResponse;
import com.dmytro.language_learning_api.service.WordsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/words")
@AllArgsConstructor
@Validated
public class WordsController {

    public final WordsService wordsService;

    @Transactional(readOnly = true)
    @GetMapping("/{wordId}")
    public ResponseEntity<WordsDTO> getWordsById(@PathVariable UUID wordId) {
        return ResponseEntity.ok(wordsService.getWordById(wordId));
    }

    @GetMapping("/user")
    public ResponseEntity<PageResponse<WordsDTO>> getAllWordsByOwnerEmail(
            Authentication authentication,
            @RequestParam(defaultValue = "0", required = false)int pageNo,
            @RequestParam(defaultValue = "10", required = false)int pageSize
    ) {
        return new ResponseEntity<>(
                wordsService
                        .getAllWordsByOwnerEmail(
                                authentication.getName()
                                , pageNo
                                , pageSize
                        )
                , HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<WordsDTO> createWord(@Valid @RequestBody CreateWordRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(wordsService.createWord(request)
                );
    }

    @PutMapping("/update-word/{wordId}")
    public ResponseEntity<WordsDTO> updateOriginalWordById(
            @PathVariable UUID wordId,
            @RequestBody UpdateWordRequest updateWordRequest
    ) {
        return ResponseEntity.ok(wordsService.updateWord(wordId, updateWordRequest));
    }

    @PutMapping("/add-translation-to-word/{wordId}")
    public ResponseEntity<WordsDTO> addTranslationToWordById(
            @PathVariable UUID wordId,
            @Valid @RequestBody TranslationDTO translationDto
    ) {
        return  ResponseEntity.ok(wordsService.addTranslationToWord(wordId, translationDto));
    }

    @PostMapping("/{wordId}/synonyms/{synonymId}")
    public ResponseEntity<Void> addSynonym(
            @PathVariable UUID wordId,
            @PathVariable UUID synonymId
    ) {
        wordsService.addSynonym(wordId, synonymId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-word/{wordId}")
    public ResponseEntity<Void> deleteWordById(@PathVariable UUID wordId) {
        wordsService.deleteWord(wordId);
        return ResponseEntity.noContent().build();
    }
}

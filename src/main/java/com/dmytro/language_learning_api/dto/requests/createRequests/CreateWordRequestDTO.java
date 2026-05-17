package com.dmytro.language_learning_api.dto.requests.createRequests;

import com.dmytro.language_learning_api.dto.TranslationDTO;
import jakarta.annotation.Nullable;

import java.util.List;

public record CreateWordRequestDTO(
        String sourceLanguage,
        String originalWord,
        @Nullable
        List<TranslationDTO> translations
) {
}

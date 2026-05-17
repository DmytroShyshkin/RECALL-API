package com.dmytro.language_learning_api.dto;

import com.dmytro.language_learning_api.model.Users;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record WordsDTO(
        UUID id,
        @NotBlank
        @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language must be an ISO code like 'en' or 'en-US'")
        String sourceLanguage,
        @NotBlank
        @Size(min = 1, max = 100)
        String originalWord,
        //@NotNull(message = "Owner id is required")
        //UUID ownerId,
        @Nullable
        Set<UUID> synonymIds,
        @Nullable
        List<TranslationDTO> translations
        )
        // translations optional on create; validate nested DTOs with @Valid in controller if List<TranslationDTO>
        //List<TranslationDTO> translations)
        {
}
package com.dmytro.language_learning_api.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EnrichmentRequest(
    String word
    , @JsonProperty("source_language") String sourceLanguage
    , @JsonProperty("target_language") String targetLanguage
) {
}

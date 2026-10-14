package com.dmytro.language_learning_api.dto.requests;

public record EnrichmentRequest(
    String word
    , String sourceLanguage
    , String targetLanguage
) {
}

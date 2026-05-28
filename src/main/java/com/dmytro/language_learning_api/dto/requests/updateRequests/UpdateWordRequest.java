package com.dmytro.language_learning_api.dto.requests.updateRequests;

public record UpdateWordRequest(
        String sourceLanguage,
        String originalWord
) {
}

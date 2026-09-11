package com.dmytro.language_learning_api.kafka.producer.word;

import java.util.List;
import java.util.UUID;

public record WordUpsertedEvent(
        UUID wordId
        , String userEmail
        , String sourceLanguage
        , String originalWord
        , List<TranslationPayload> translations
) {
    public record TranslationPayload(
            UUID id
            , String targetLanguage
            , String translatedWord
            , String description
    ) {
    }
}
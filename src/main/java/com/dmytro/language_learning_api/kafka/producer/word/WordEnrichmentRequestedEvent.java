package com.dmytro.language_learning_api.kafka.producer.word;

import java.util.List;
import java.util.UUID;

public record WordEnrichmentRequestedEvent(
    UUID wordId
    , String sourceLanguage
    , String originalWord
    , List<Translations> translations
) {
     public record Translations(
        UUID translationId
        , String targetLanguage
        , String translatedWord
        , String description
    ) {
    }
}

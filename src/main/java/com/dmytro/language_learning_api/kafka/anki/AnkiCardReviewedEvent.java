package com.dmytro.language_learning_api.kafka.anki;

import java.util.UUID;

public record AnkiCardReviewedEvent(
        UUID cardId,
        UUID wordId,
        String word,
        String userEmail,
        int rating,
        String newState
) {}

package com.dmytro.language_learning_api.kafka;

import java.util.UUID;

public record AnkiCardReviewedEvent(
        UUID cardId,
        UUID wordId,
        String userEmail,
        int rating,
        String newState
) {}

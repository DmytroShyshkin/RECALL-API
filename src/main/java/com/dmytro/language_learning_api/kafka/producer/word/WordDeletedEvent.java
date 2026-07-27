package com.dmytro.language_learning_api.kafka.producer.word;

import java.util.UUID;

public record WordDeletedEvent(
        UUID wordId
        , String userEmail
) {
}

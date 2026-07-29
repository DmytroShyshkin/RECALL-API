package com.dmytro.language_learning_api.kafka.producer.userDelete;

public record UserDeleteEvent(
        String userEmail
) {
}

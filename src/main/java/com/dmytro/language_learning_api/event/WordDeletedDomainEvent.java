package com.dmytro.language_learning_api.event;

import java.util.UUID;

public record WordDeletedDomainEvent(
    UUID wordId
    , String userEmail) {
}

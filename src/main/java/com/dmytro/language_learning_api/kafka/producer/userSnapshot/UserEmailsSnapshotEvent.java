package com.dmytro.language_learning_api.kafka.producer.userSnapshot;

import java.time.Instant;
import java.util.List;

public record UserEmailsSnapshotEvent(
    List<String> emails
    , Instant generatedAt
) {
}

package com.dmytro.language_learning_api.kafka.producer.userSnapshot;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Also carries the IDs of all live words: the Kafka plan has no room for a separate topic,
// and the quiz service uses them to reconcile word snapshots and Anki cards.
public record UserEmailsSnapshotEvent(
    List<String> emails
    , List<UUID> wordIds
    , Instant generatedAt
) {
}

package com.dmytro.language_learning_api.kafka.producer.userSnapshot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSnapshotProducer {
    private static final String TOPIC = "recall.user.snapshot";
    private static final String SNAPSHOT_KEY = "snapshot";

    @Qualifier("userSnapshotKafkaTemplate")
    private final KafkaTemplate<String, UserEmailsSnapshotEvent> kafkaTemplate;

    public void publishSnapshot(UserEmailsSnapshotEvent event) {
        kafkaTemplate.send(TOPIC, SNAPSHOT_KEY, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish {} for {}: {}",
                                event.getClass().getSimpleName(), SNAPSHOT_KEY, ex.getMessage(), ex);
                    } else {
                        log.info("{} published for {}, partition={}, offset={}",
                                event.getClass().getSimpleName(), SNAPSHOT_KEY,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

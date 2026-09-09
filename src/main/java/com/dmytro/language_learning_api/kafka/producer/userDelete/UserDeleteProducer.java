package com.dmytro.language_learning_api.kafka.producer.userDelete;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeleteProducer {
    private static final String TOPIC = "recall.user.delete";
    @Qualifier("userDeletedKafkaTemplate")
    private final KafkaTemplate<String, UserDeleteEvent> kafkaTemplate;

    public void sendDeletedUserEvent(UserDeleteEvent event) {
        kafkaTemplate.send(TOPIC, event.userEmail(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish UserDeleteEvent for {}: {}",
                                event.userEmail(), ex.getMessage(), ex);
                    } else {
                        log.info("UserDeleteEvent published for {}, partition={}, offset={}",
                                event.userEmail(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

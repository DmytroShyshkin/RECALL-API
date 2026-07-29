package com.dmytro.language_learning_api.kafka.producer.userDelete;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeleteProducer {
    private static final String TOPIC = "recall.user.delete";
    @Qualifier("userDeletedKafkaTemplate")
    private final KafkaTemplate<String, UserDeleteEvent> kafkaTemplate;

    public void sendDeletedUserEvent(UserDeleteEvent event) {
        kafkaTemplate.send(TOPIC, event.userEmail(), event);
    }
}

package com.dmytro.language_learning_api.kafka.producer.userDelete;

import com.dmytro.language_learning_api.kafka.producer.word.WordDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeleteProducer {
    private static final String TOPIC = "recall.user.delete";
    private final KafkaTemplate<String, UserDeleteEvent> kafkaTemplate;

    public void sendDeleteduserEvent(UserDeleteEvent event) {
        kafkaTemplate.send(TOPIC, event.userEmail, event);
    }
}

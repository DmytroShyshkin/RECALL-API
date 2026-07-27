package com.dmytro.language_learning_api.kafka.producer.word;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordDeletedProducer {
    private static final String TOPIC = "recall.delete.word";
    private final KafkaTemplate<String, WordDeletedEvent> kafkaTemplate;

    public void sendDeletedEvent(WordDeletedEvent event) {
        kafkaTemplate.send(TOPIC, event.wordId().toString(), event);
    }
}

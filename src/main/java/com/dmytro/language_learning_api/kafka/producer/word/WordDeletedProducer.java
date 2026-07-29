package com.dmytro.language_learning_api.kafka.producer.word;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordDeletedProducer {
    private static final String TOPIC = "recall.delete.word";
    @Qualifier("wordDeletedKafkaTemplate")
    private final KafkaTemplate<String, WordDeletedEvent> kafkaTemplate;

    public void sendDeletedWordEvent(WordDeletedEvent event) {
        kafkaTemplate.send(TOPIC, event.wordId().toString(), event);
    }
}

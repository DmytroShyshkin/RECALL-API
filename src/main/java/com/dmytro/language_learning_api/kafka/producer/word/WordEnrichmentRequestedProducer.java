package com.dmytro.language_learning_api.kafka.producer.word;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordEnrichmentRequestedProducer {
    private static final String TOPIC = "recall.word.enrichment";

    @Qualifier("wordEnrichmentRequestedKafkaTemplate")
    private final KafkaTemplate<String, WordEnrichmentRequestedEvent> kafkaTemplate;

    public void sendWordEnrichmentRequestedEvent(WordEnrichmentRequestedEvent event) {
        kafkaTemplate.send(TOPIC, event.wordId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish WordEnrichmentRequestedEvent for {}: {}",
                                event.wordId(), ex.getMessage(), ex);
                    } else {
                        log.info("WordEnrichmentRequestedEvent published for {}, partition={}, offset={}",
                                event.wordId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

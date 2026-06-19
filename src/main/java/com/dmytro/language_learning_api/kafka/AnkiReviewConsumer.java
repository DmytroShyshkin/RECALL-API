package com.dmytro.language_learning_api.kafka;

import com.dmytro.language_learning_api.service.statistics.WordStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnkiReviewConsumer {

    private final WordStatisticsService statisticsService;

    @KafkaListener(topics = "anki.card.reviewed", groupId = "main-backend")
    public void handleReview(AnkiCardReviewedEvent event) {
        statisticsService.processReviewEvent(event);
    }
}
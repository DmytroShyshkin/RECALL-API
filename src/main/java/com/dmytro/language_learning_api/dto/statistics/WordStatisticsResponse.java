package com.dmytro.language_learning_api.dto.statistics;

import java.time.LocalDateTime;
import java.util.UUID;

public record WordStatisticsResponse(
        UUID wordId
        , int totalReviews
        , int correctReviews
        , int lapses
        , LocalDateTime lastReviewedAt
) {
}

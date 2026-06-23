package com.dmytro.language_learning_api.dto.statistics;

public record UserStatsSummaryResponse(
        int currentStreak
        , int totalWordsLearned
        , int totalReviews
) {
}

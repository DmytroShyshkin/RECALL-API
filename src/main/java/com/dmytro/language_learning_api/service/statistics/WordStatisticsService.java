package com.dmytro.language_learning_api.service.statistics;

import com.dmytro.language_learning_api.kafka.AnkiCardReviewedEvent;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.model.statistics.UserActivity;
import com.dmytro.language_learning_api.model.statistics.WordReviewLog;
import com.dmytro.language_learning_api.model.statistics.WordStatistics;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.statistics.UserActivityRepository;
import com.dmytro.language_learning_api.repository.statistics.WordReviewLogRepository;
import com.dmytro.language_learning_api.repository.statistics.WordStatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WordStatisticsService {

    private final WordReviewLogRepository reviewLogRepository;
    private final WordStatisticsRepository statisticsRepository;
    private final UserActivityRepository userActivityRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void processReviewEvent(AnkiCardReviewedEvent event) {
        Users user = usersRepository.findByEmail(event.userEmail())
                .orElseThrow(() -> new IllegalStateException("User not found: " + event.userEmail()));

        LocalDateTime now = LocalDateTime.now();

        // 1. Сохраняем лог
        reviewLogRepository.save(WordReviewLog.builder()
                .cardId(event.cardId())
                .wordId(event.wordId())
                .user(user)
                .rating(event.rating())
                .newState(event.newState())
                .reviewedAt(now)
                .build());

        // 2. Обновляем агрегированную статистику по слову
        WordStatistics stats = statisticsRepository
                .findByWordIdAndUserId(event.wordId(), user.getId())
                .orElseGet(() -> WordStatistics.builder()
                        .wordId(event.wordId())
                        .user(user)
                        .totalReviews(0)
                        .correctReviews(0)
                        .lapses(0)
                        .build());

        stats.setTotalReviews(stats.getTotalReviews() + 1);
        if (event.rating() >= 3) {
            stats.setCorrectReviews(stats.getCorrectReviews() + 1);
        }
        if (event.rating() == 1) {
            stats.setLapses(stats.getLapses() + 1);
        }
        stats.setLastReviewedAt(now);

        statisticsRepository.save(stats);

        // 3. Обновляем активность за сегодня (для streak)
        LocalDate today = LocalDate.now();
        UserActivity activity = userActivityRepository
                .findByUserIdAndActivityDate(user.getId(), today)
                .orElseGet(() -> UserActivity.builder()
                        .user(user)
                        .activityDate(today)
                        .reviewsCount(0)
                        .build());

        activity.setReviewsCount(activity.getReviewsCount() + 1);
        userActivityRepository.save(activity);
    }
}
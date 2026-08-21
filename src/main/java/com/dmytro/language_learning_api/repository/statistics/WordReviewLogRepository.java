package com.dmytro.language_learning_api.repository.statistics;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmytro.language_learning_api.model.statistics.WordReviewLog;

public interface WordReviewLogRepository extends JpaRepository<WordReviewLog, UUID> {
    List<WordReviewLog> findByUserIdOrderByReviewedAtDesc(UUID userId);
    List<WordReviewLog> findByWordIdAndUserId(UUID wordId, UUID userId);

    void deleteByUserId(UUID userId);
}
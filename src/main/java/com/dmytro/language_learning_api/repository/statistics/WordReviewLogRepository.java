package com.dmytro.language_learning_api.repository.statistics;

import com.dmytro.language_learning_api.model.statistics.WordReviewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WordReviewLogRepository extends JpaRepository<WordReviewLog, UUID> {
    List<WordReviewLog> findByUserIdOrderByReviewedAtDesc(UUID userId);
    List<WordReviewLog> findByWordIdAndUserId(UUID wordId, UUID userId);
}
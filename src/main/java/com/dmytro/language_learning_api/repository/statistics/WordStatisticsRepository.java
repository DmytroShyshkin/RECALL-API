package com.dmytro.language_learning_api.repository.statistics;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmytro.language_learning_api.model.statistics.WordStatistics;

public interface WordStatisticsRepository extends JpaRepository<WordStatistics, UUID> {
    void deleteByWordId(UUID wordId);
    Optional<WordStatistics> findByWordIdAndUserId(UUID wordId, UUID userId);
    List<WordStatistics> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
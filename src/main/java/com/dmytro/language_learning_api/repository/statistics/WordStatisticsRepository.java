package com.dmytro.language_learning_api.repository.statistics;

import com.dmytro.language_learning_api.model.statistics.WordStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordStatisticsRepository extends JpaRepository<WordStatistics, UUID> {
    Optional<WordStatistics> findByWordIdAndUserId(UUID wordId, UUID userId);
    List<WordStatistics> findByUserId(UUID userId);
}
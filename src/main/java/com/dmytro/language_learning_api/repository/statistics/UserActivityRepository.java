package com.dmytro.language_learning_api.repository.statistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmytro.language_learning_api.model.statistics.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    Optional<UserActivity> findByUserIdAndActivityDate(UUID userId, LocalDate date);
    List<UserActivity> findByUserIdOrderByActivityDateDesc(UUID userId);

    void deleteByUserId(UUID userId);
}
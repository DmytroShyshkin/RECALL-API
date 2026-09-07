package com.dmytro.language_learning_api.service.scheduler;

import java.time.Duration;
import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dmytro.language_learning_api.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnverifiedAccountsCleanupJob {
    private final UsersRepository usersRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteUnverifiedAccounts() {
        Instant cutoff = Instant.now().minus(Duration.ofHours(24));
        long deletedCount = usersRepository.deleteAllByEmailVerifiedFalseAndCreatedAtBefore(cutoff);
        log.info("Deleted {} unverified accounts", deletedCount);
    }
}

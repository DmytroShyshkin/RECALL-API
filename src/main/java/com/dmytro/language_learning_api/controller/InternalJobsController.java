package com.dmytro.language_learning_api.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmytro.language_learning_api.service.scheduler.UnverifiedAccountsCleanupJob;
import com.dmytro.language_learning_api.service.scheduler.UserSnapshotPublishJob;
import com.dmytro.language_learning_api.service.scheduler.WordSnapshotPublishJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/jobs")
@RequiredArgsConstructor
@Slf4j
public class InternalJobsController {

    private static final String SECRET_HEADER = "X-Internal-Job-Secret";

    private final UnverifiedAccountsCleanupJob cleanupJob;
    private final UserSnapshotPublishJob userSnapshotJob;
    private final WordSnapshotPublishJob wordSnapshotJob;

    @Value("${internal.jobs.secret}")
    private String expectedSecret;

    @PostMapping("/cleanup-unverified-accounts")
    public ResponseEntity<Void> triggerCleanupUnverifiedAccounts(
            @RequestHeader(value = SECRET_HEADER, required = false) String providedSecret) {
        if (!isAuthorized(providedSecret)) {
            log.warn("Rejected unauthorized call to cleanup-unverified-accounts");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Internal trigger: cleanup unverified accounts");
        cleanupJob.deleteUnverifiedAccounts();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publish-user-snapshot")
    public ResponseEntity<Void> triggerPublishUserSnapshot(
            @RequestHeader(value = SECRET_HEADER, required = false) String providedSecret) {
        if (!isAuthorized(providedSecret)) {
            log.warn("Rejected unauthorized call to publish-user-snapshot");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Internal trigger: publish user snapshot");
        userSnapshotJob.publishSnapshot();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publish-word-snapshot")
    public ResponseEntity<Void> triggerPublishWordSnapshot(
            @RequestHeader(value = SECRET_HEADER, required = false) String providedSecret) {
        if (!isAuthorized(providedSecret)) {
            log.warn("Rejected unauthorized call to publish-word-snapshot");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Internal trigger: publish word snapshot");
        wordSnapshotJob.publishAllWords();
        return ResponseEntity.ok().build();
    }

    private boolean isAuthorized(String providedSecret) {
        if (providedSecret == null || expectedSecret == null || expectedSecret.isBlank()) {
            return false;
        }

        byte[] provided = providedSecret.getBytes(StandardCharsets.UTF_8);
        byte[] expected = expectedSecret.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(provided, expected);
    }
}

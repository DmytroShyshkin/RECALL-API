package com.dmytro.language_learning_api.service.scheduler;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dmytro.language_learning_api.kafka.producer.userSnapshot.UserEmailsSnapshotEvent;
import com.dmytro.language_learning_api.kafka.producer.userSnapshot.UserSnapshotProducer;
import com.dmytro.language_learning_api.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSnapshotPublishJob {

    private final UsersRepository usersRepository;
    private final UserSnapshotProducer producer;

    @Scheduled(cron = "0 0 3 * * *")
    public void publishSnapshot() {
        var emails = usersRepository.findAllEmails();

        if (!emails.isEmpty()) {
            var event = new UserEmailsSnapshotEvent(emails, Instant.now());
            producer.publishSnapshot(event);
        }
    }
}

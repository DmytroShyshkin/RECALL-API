package com.dmytro.language_learning_api.service.scheduler;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dmytro.language_learning_api.kafka.producer.userSnapshot.UserEmailsSnapshotEvent;
import com.dmytro.language_learning_api.kafka.producer.userSnapshot.UserSnapshotProducer;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.WordsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSnapshotPublishJob {

    private final UsersRepository usersRepository;
    private final WordsRepository wordsRepository;
    private final UserSnapshotProducer producer;

    @Scheduled(cron = "0 0 3 * * *")
    public void publishSnapshot() {
        // Taken before reading: anything created after this moment is not in the lists,
        // and the consumer must treat it as fresh rather than orphaned.
        var generatedAt = Instant.now();
        var emails = usersRepository.findAllEmails();
        var wordIds = wordsRepository.findAllIds();

        if (!emails.isEmpty()) {
            var event = new UserEmailsSnapshotEvent(emails, wordIds, generatedAt);
            producer.publishSnapshot(event);
        }
    }
}

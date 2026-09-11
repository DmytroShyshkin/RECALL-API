package com.dmytro.language_learning_api.event.userDelete;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dmytro.language_learning_api.event.UserDeletedDomainEvent;
import com.dmytro.language_learning_api.kafka.producer.userDelete.UserDeleteEvent;
import com.dmytro.language_learning_api.kafka.producer.userDelete.UserDeleteProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDeletedEventListener {
    private final UserDeleteProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserDeleted(UserDeletedDomainEvent event) {
        producer.sendDeletedUserEvent(new UserDeleteEvent(event.userEmail()));
    }
}
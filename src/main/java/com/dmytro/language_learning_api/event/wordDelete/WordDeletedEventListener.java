package com.dmytro.language_learning_api.event.wordDelete;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dmytro.language_learning_api.kafka.producer.word.WordDeletedEvent;
import com.dmytro.language_learning_api.kafka.producer.word.WordDeletedProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WordDeletedEventListener {
    private final WordDeletedProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onWordDeleted(WordDeletedEvent event) {
        producer.sendDeletedWordEvent(new WordDeletedEvent(event.wordId(), event.userEmail()));
    }
}

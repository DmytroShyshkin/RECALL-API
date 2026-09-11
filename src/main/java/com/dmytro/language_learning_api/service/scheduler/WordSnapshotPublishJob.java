package com.dmytro.language_learning_api.service.scheduler;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dmytro.language_learning_api.kafka.producer.word.WordUpsertedProducer;
import com.dmytro.language_learning_api.mapper.WordUpsertedEventMapper;
import com.dmytro.language_learning_api.model.Words;
import com.dmytro.language_learning_api.repository.WordsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordSnapshotPublishJob {

    private static final int PAGE_SIZE = 200;

    private final WordsRepository wordsRepository;
    private final WordUpsertedProducer producer;
    private final WordUpsertedEventMapper wordUpsertedEventMapper;

    @Scheduled(cron = "0 30 3 * * *")
    public void publishAllWords() {
        int pageNo = 0;
        Page<Words> page;

        do {
            Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
            page = wordsRepository.findAllWithTranslations(pageable);

            page.getContent().forEach(word ->
                    producer.sendWordUpsertedEvent(wordUpsertedEventMapper.toEvent(word)));

            log.info("WordSnapshotPublishJob: published page {} of {} ({} words)",
                    pageNo, page.getTotalPages(), page.getNumberOfElements());

            pageNo++;
        } while (page.hasNext());
    }
}
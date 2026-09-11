package com.dmytro.language_learning_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dmytro.language_learning_api.model.Translation;

public interface TranslationRepository extends JpaRepository<Translation, UUID> {
    Page<Translation> findByWordId(UUID wordId, Pageable pageable);
    Page<Translation> findByWordIdAndWordOwnerId(UUID wordId, UUID ownerId, Pageable pageable);
    Optional<Translation> findByIdAndWordOwnerId(UUID translationId, UUID ownerId);
    List<Translation> findByTargetLanguageAndWordOwnerId(String targetLanguage, UUID ownerId);
}

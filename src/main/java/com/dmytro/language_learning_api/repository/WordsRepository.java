package com.dmytro.language_learning_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dmytro.language_learning_api.model.Words;

public interface WordsRepository extends JpaRepository<Words, UUID> {
    @Query("""
       SELECT w
       FROM Words w
       LEFT JOIN FETCH w.synonyms
       WHERE w.owner.id = :ownerId
       """)
    Page<Words> findByOwnerId(UUID ownerId, Pageable pageable);

    void deleteByOwnerId(UUID ownerId);

    @Query("""
       SELECT w
       FROM Words w
       LEFT JOIN FETCH w.translations
       LEFT JOIN FETCH w.owner
       WHERE w.id = :wordId
       """)
    Optional<Words> findByIdWithTranslations(UUID wordId);

    @Query(value = """
       SELECT DISTINCT w
       FROM Words w
       LEFT JOIN FETCH w.translations
       LEFT JOIN FETCH w.owner
       """,
       countQuery = "SELECT COUNT(w) FROM Words w")
    Page<Words> findAllWithTranslations(Pageable pageable);
}

package com.dmytro.language_learning_api.repository;

import com.dmytro.language_learning_api.model.Words;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WordsRepository extends JpaRepository<Words, UUID> {
    @Query("""
       SELECT w
       FROM Words w
       LEFT JOIN FETCH w.synonyms
       WHERE w.owner.id = :ownerId
       """)
    Page<Words> findByOwnerId(UUID ownerId, Pageable pageable);

    void deleteByOwnerId(UUID ownerId);
}

package com.dmytro.language_learning_api.model;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "translations",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"word_id", "target_language", "translated_word"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Translation {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    // lenguaje de traducción: "es", "uk" y etc.
    @Column(name = "target_language", nullable = false, length = 10)
    private String targetLanguage;

    @Column(nullable = false)
    private String translatedWord;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Words word;

    // No fixed length here on purpose — this is a free-text note field, and a
    // plain @Column(nullable = true) defaults to varchar(255) in Postgres,
    // which is easy to exceed by accident. columnDefinition = "TEXT" removes
    // the limit at the DB level (paired with the @Size check on the DTO,
    // which now rejects an oversized value with a clean 400 instead of this
    // silently succeeding past validation and blowing up as an unhandled
    // DataIntegrityViolationException on the INSERT).
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    // recurso (user/manual, deepl, openai)
    @Column(nullable = true)
    private String source;
}

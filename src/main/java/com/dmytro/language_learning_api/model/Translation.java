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
        uniqueConstraints = {@UniqueConstraint(columnNames = {"word_id", "target_language", "text"})}
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

    @Column(nullable = true)
    private String description;

    // recurso (user/manual, deepl, openai)
    @Column(nullable = true)
    private String source;
}

package com.dmytro.language_learning_api.model.statistics;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import com.dmytro.language_learning_api.model.Users;

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
@Table(name = "word_statistics",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"word_id", "user_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordStatistics {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "word_id", nullable = false)
    private UUID wordId;

    @Column(nullable = false)
    private String word;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Column(nullable = false)
    private int totalReviews;

    @Column(nullable = false)
    private int correctReviews;

    @Column(nullable = false)
    private int lapses;

    @Column(nullable = true)
    private LocalDateTime lastReviewedAt;
}
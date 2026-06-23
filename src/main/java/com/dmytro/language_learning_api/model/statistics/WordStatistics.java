package com.dmytro.language_learning_api.model.statistics;

import com.dmytro.language_learning_api.model.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
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
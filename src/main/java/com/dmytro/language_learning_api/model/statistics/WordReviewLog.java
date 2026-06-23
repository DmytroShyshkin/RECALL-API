package com.dmytro.language_learning_api.model.statistics;

import com.dmytro.language_learning_api.model.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "word_review_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordReviewLog {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private UUID cardId;

    @Column(nullable = false)
    private UUID wordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String newState;

    @Column(nullable = false)
    private LocalDateTime reviewedAt;
}
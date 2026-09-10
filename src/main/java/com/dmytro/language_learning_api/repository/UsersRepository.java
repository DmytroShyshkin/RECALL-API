package com.dmytro.language_learning_api.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dmytro.language_learning_api.model.Users;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);

    @Query("SELECT u.email FROM Users u")
    List<String> findAllEmails();

    Optional<Users> findByVerificationToken(String token);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    long deleteAllByEmailVerifiedFalseAndCreatedAtBefore(Instant cutoff);
}

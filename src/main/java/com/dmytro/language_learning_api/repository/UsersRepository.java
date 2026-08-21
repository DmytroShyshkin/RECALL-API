package com.dmytro.language_learning_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmytro.language_learning_api.model.Users;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);

    Optional<Users> findByVerificationToken(String token);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

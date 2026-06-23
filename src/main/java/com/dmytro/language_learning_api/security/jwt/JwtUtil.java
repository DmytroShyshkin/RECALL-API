package com.dmytro.language_learning_api.security.jwt;

import com.dmytro.language_learning_api.exception.NotFoundException.UserNotFoundException;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UsersRepository usersRepository;

    public Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = auth.getName();

        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new RuntimeException("No authenticated user");
        }

        return auth.getName(); // return email
    }
}

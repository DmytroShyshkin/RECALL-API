package com.dmytro.language_learning_api.service.securityService;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.authentication.AuthResponse;
import com.dmytro.language_learning_api.dto.authentication.LoginRequest;
import com.dmytro.language_learning_api.exception.ConflictException.ConflictException;
import com.dmytro.language_learning_api.exception.NotFoundException.NotFoundException;
import com.dmytro.language_learning_api.exception.NotFoundException.UserNotFoundException;
import com.dmytro.language_learning_api.model.Role;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public AuthResponse register(UsersDTO dto) {
        String verificationToken = UUID.randomUUID().toString();
        Instant tokenExpiresAt = Instant.now().plus(Duration.ofHours(2));

        Users user = Users.builder()
                .email(dto.email())
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .emailVerified(false)
                .verificationToken(verificationToken)
                .tokenExpiresAt(tokenExpiresAt)
                .build();

        usersRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());

        return new AuthResponse(
                user.getId(),
                accessToken,
                null,
                user.getEmail(),
                user.getUsername()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Users user = usersRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());

        return new AuthResponse(
                user.getId(),
                accessToken,
                null,
                user.getEmail(),
                user.getUsername()
        );
    }

    @Override
    public void resendVerificationEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (user.isEmailVerified()) {
            throw new ConflictException("Email already verified.");
        }

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiresAt(Instant.now().plus(Duration.ofHours(2)));
        usersRepository.save(user);

        emailService.sendVerificationEmail(email, token);
        user.setTokenExpiresAt(Instant.now().plus(Duration.ofHours(2)));
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);

        if (!jwtService.isValid(refreshToken, email)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Users user = usersRepository.findByEmail(email)
                .orElseThrow();

        String newAccessToken =
                jwtService.generateAccessToken(user.getEmail(), user.getRole());

        return new AuthResponse(
                user.getId(),
                newAccessToken,
                refreshToken,
                user.getEmail(),
                user.getUsername()
        );
    }

    @Override
    public void verifyEmail(String token) {
        Users user = usersRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid verification token."));

        if(user.getTokenExpiresAt().isBefore(Instant.now())) {
                throw new ConflictException("Verification token has expired.");
        }

        user.setEmailVerified(true);

        user.setVerificationToken(null);
        user.setTokenExpiresAt(null);

        usersRepository.save(user);
    }
}

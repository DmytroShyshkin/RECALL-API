package com.dmytro.language_learning_api.controller;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.authentication.AuthResponse;
import com.dmytro.language_learning_api.dto.authentication.LoginRequest;
import com.dmytro.language_learning_api.exception.ConflictException.ConflictException;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.security.jwt.JwtService;
import com.dmytro.language_learning_api.service.securityService.AuthService;
import com.dmytro.language_learning_api.service.securityService.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UsersRepository usersRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UsersDTO dto) {
        if (usersRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email already in use.");
        }
        if (usersRepository.existsByUsername(dto.username())) {
            throw new ConflictException("Username already in use.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        String refreshToken = jwtService.generateRefreshToken(request.email());

        cookieService.addRefreshToken(response, refreshToken);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        String refreshToken = cookieService.extractRefreshToken(request);

        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(Authentication authentication) {
        authService.resendVerificationEmail(authentication.getName());
        return ResponseEntity.ok().build();
    }
}

package com.dmytro.language_learning_api.service.securityService;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.authentication.AuthResponse;
import com.dmytro.language_learning_api.dto.authentication.LoginRequest;

public interface AuthService {
    AuthResponse register(UsersDTO dto);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(String refreshToken);
    void resendVerificationEmail(String email);
    void verifyEmail(String token);
}

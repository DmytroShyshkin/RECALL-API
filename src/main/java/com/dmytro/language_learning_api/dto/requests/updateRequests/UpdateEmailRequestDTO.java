package com.dmytro.language_learning_api.dto.requests.updateRequests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequestDTO(
        @NotBlank(message = "Email must be valid")
        @Email(message = "Email must be valid")
        String email
) {
}

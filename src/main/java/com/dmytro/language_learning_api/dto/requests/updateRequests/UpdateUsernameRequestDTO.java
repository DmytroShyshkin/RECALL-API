package com.dmytro.language_learning_api.dto.requests.updateRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequestDTO(
        @NotBlank(message = "Username must be valid")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        String username
) {
}

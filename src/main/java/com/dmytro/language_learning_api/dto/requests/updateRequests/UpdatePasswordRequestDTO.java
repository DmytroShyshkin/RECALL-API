package com.dmytro.language_learning_api.dto.requests.updateRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequestDTO(
        @NotBlank(message = "Old password must be valid")
        String oldPassword
        ,
        @NotBlank(message = "New password must be valid")
        @Size(min = 8, message = "New password must be at least 8 characters")
        String newPassword
) {
}

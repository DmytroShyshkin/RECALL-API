package com.dmytro.language_learning_api.dto.authentication;

import com.dmytro.language_learning_api.model.Role;

public record RegisterResponse(
    String email
    , String username
    , Role role
) {
}

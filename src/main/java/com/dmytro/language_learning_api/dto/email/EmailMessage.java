package com.dmytro.language_learning_api.dto.email;

public record EmailMessage(
    String to
    , String subject
    , String htmlBody
) {
}

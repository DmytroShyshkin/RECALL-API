package com.dmytro.language_learning_api.dto.email;

import java.util.List;

public record BrevoSendEmailRequest(
    Sender sender
    , List<Recipient> to
    , String subject
    , String htmlContent
) {
    public record Sender(String email, String name) {}
    public record Recipient(String email) {}
}

package com.dmytro.language_learning_api.service.mail;

import com.dmytro.language_learning_api.dto.email.EmailMessage;

public interface EmailSender {
    void send(EmailMessage message);
}

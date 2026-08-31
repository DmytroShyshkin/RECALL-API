package com.dmytro.language_learning_api.service.securityService;

import org.springframework.stereotype.Service;

import com.dmytro.language_learning_api.config.email.EmailProperties;
import com.dmytro.language_learning_api.dto.email.EmailMessage;
import com.dmytro.language_learning_api.service.mail.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailSender emailSender;
    
    private final EmailProperties properties;

    public void sendVerificationEmail(String to, String token) {
        
        String link = properties.getFrontUrl() + "/verify-email?token=" + token;

        String htmlBody = """
                <p>Hello, welcome to Recall!</p>
                <p>Verify your email for get started!</p>
                <a href="%s">%s</a>
                """.formatted(link, "Verify Email Address");

        EmailMessage message = new EmailMessage(
            to
            , "Verify your email"
            , htmlBody
        );

        emailSender.send(message);
    }
}

package com.dmytro.language_learning_api.service.mail;

import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.dmytro.language_learning_api.config.email.EmailProperties;
import com.dmytro.language_learning_api.dto.email.BrevoSendEmailRequest;
import com.dmytro.language_learning_api.dto.email.EmailMessage;
import com.dmytro.language_learning_api.exception.emailException.EmailSendException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrevoEmailSender implements EmailSender {

    private final WebClient brevoWebClient;
    private final EmailProperties properties;

    @Override
    public void send(EmailMessage message) {
        BrevoSendEmailRequest request = new BrevoSendEmailRequest(
                new BrevoSendEmailRequest.Sender(properties.getFromAddress(), properties.getFromName()),
                List.of(new BrevoSendEmailRequest.Recipient(message.to())),
                message.subject(),
                message.htmlBody()
        );

        try {
            brevoWebClient.post()
                .uri("/smtp/email")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new EmailSendException(
                                        "Brevo responde with error %s: %s".formatted(response.statusCode(), body)))))
                .toBodilessEntity()
                .block();

            log.info("Email sent to {}", mask(message.to()));

    } catch (WebClientResponseException e) {
        log.error("Brevo request failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
        throw new EmailSendException("Failed to send email via Brevo", e);
    } catch (Exception e) {
        log.error("Unexpected error while sending email via Brevo", e);
        throw new EmailSendException("Failed to send email via Brevo", e);
    }
}

    private static String mask(String email) {
        int at = email.indexOf('@');
        return at <= 1 ? "***" : email.charAt(0) + "***" + email.substring(at);
    }
}

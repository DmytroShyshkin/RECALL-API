package com.dmytro.language_learning_api.config.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BrevoWebClientConfig {
    
    @Bean
    public WebClient brevoWebClient(EmailProperties emailProperties) {
        return WebClient.builder()
            .baseUrl("https://api.brevo.com/v3")
            .defaultHeader("api-key", emailProperties.getBrevoApiKey())
            .build();
    }
}

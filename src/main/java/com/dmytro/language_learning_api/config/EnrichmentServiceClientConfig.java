package com.dmytro.language_learning_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration 
public class EnrichmentServiceClientConfig {
    // Default keeps the app bootable even when the env var isn't set yet
    // (e.g. before the enrichment service has a real deployed address) —
    // only the /enrich call itself would fail, not the whole context.
    @Value("${enrichment-service.base-url:http://localhost:8000}")
    private String baseUrl;

    @Bean
    public RestClient enrichmentServiceRestClient(RestClient.Builder builder) {
        return builder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}

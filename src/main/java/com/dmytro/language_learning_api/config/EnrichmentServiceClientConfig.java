package com.dmytro.language_learning_api.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;

@Configuration 
public class EnrichmentServiceClientConfig {
    // Default keeps the app bootable even when the env var isn't set yet
    // (e.g. before the enrichment service has a real deployed address) —
    // only the /enrich call itself would fail, not the whole context.
    @Value("${enrichment-service.base-url:http://localhost:8000}")
    private String baseUrl;

    // Default timeouts are set to 5 seconds, but can be overridden by environment variables.
    @Value("${enrichment-service.connect-timeout-ms:5000}")
    private int connectTimeout;
    @Value("${enrichment-service.read-timeout-ms:5000}")
    private int readTimeout;

    @Bean
    public RestClient enrichmentServiceRestClient(RestClient.Builder builder) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
            .withConnectTimeout(Duration.ofMillis(connectTimeout))
            .withReadTimeout(Duration.ofMillis(readTimeout));

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect().build(settings);

        return builder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .requestFactory(requestFactory)
            .build();
    }
}

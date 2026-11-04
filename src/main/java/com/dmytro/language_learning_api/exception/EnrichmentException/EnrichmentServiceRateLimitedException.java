package com.dmytro.language_learning_api.exception.EnrichmentException;

public class EnrichmentServiceRateLimitedException extends RuntimeException {
    public EnrichmentServiceRateLimitedException() {
        super("Enrichment service is rate limited");
    }

}

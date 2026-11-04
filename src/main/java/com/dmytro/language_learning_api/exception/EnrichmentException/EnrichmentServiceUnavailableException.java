package com.dmytro.language_learning_api.exception.EnrichmentException;

public class EnrichmentServiceUnavailableException extends RuntimeException {
    public EnrichmentServiceUnavailableException() {
        super("Enrichment service is unavailable");
    }

}

package com.dmytro.language_learning_api.exception.EnrichmentException;

public class EnrichmentServiceTimeoutException extends RuntimeException {
    public EnrichmentServiceTimeoutException() {
        super("Enrichment service request timed out");
    }

}

package com.dmytro.language_learning_api.exception.emailException;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }

    public EmailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

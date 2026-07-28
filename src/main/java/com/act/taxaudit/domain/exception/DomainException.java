package com.act.taxaudit.domain.exception;

/**
 * Base exception for all domain rule violations.
 * Maps to HTTP 422 Unprocessable Entity in the API layer.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
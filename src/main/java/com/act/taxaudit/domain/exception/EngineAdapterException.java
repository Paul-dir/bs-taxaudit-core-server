package com.act.taxaudit.domain.exception;

/**
 * Exception thrown when an external engine/service adapter call fails.
 * Maps to HTTP 502 Bad Gateway in the API layer.
 */
public class EngineAdapterException extends RuntimeException {
    public EngineAdapterException(String message) {
        super(message);
    }

    public EngineAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineAdapterException(String engineName, String operation, Throwable cause) {
        super(String.format("Engine adapter [%s] failed during operation [%s]: %s", engineName, operation, cause.getMessage()), cause);
    }
}
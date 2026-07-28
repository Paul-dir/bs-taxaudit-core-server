package com.act.taxaudit.domain.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found in the API layer.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s not found with id: %s", resourceType, id));
    }
}
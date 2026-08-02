package com.mor.itas.planning.domain.exception;

public class EngineAdapterException extends RuntimeException {
    public EngineAdapterException(String message) {
        super(message);
    }
    public EngineAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}

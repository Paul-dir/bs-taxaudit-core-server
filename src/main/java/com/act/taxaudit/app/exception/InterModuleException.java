package com.act.taxaudit.app.exception;

/**
 * Exception for inter-module communication failures in ITAS ecosystem.
 * Used when a downstream module is unreachable or returns an error.
 */
public class InterModuleException extends RuntimeException {

    private final String sourceModule;
    private final String targetModule;

    public InterModuleException(String sourceModule, String targetModule, String message) {
        super(String.format("[%s -> %s] %s", sourceModule, targetModule, message));
        this.sourceModule = sourceModule;
        this.targetModule = targetModule;
    }

    public InterModuleException(String sourceModule, String targetModule, String message, Throwable cause) {
        super(String.format("[%s -> %s] %s", sourceModule, targetModule, message), cause);
        this.sourceModule = sourceModule;
        this.targetModule = targetModule;
    }

    public String getSourceModule() { return sourceModule; }
    public String getTargetModule() { return targetModule; }
}
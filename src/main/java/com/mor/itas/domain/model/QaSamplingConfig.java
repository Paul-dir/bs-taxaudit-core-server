package com.mor.itas.domain.model;

import java.time.LocalDateTime;

public class QaSamplingConfig {
    private Long id;
    private SamplingMethod samplingMethod;
    private String frequency;
    private String scopeFilters; // JSON string
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum SamplingMethod {
        RANDOM, RISK_BASED, STRATIFIED, PERCENTAGE_BASED
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SamplingMethod getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(SamplingMethod samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getScopeFilters() {
        return scopeFilters;
    }

    public void setScopeFilters(String scopeFilters) {
        this.scopeFilters = scopeFilters;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
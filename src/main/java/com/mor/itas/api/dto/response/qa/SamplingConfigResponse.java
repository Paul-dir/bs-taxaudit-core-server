package com.mor.itas.api.dto.response.qa;

import com.mor.itas.domain.model.QaSamplingConfig;
import java.time.LocalDateTime;

public class SamplingConfigResponse {
    private Long id;
    private QaSamplingConfig.SamplingMethod samplingMethod;
    private String frequency;
    private String scopeFilters;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QaSamplingConfig.SamplingMethod getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(QaSamplingConfig.SamplingMethod samplingMethod) {
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
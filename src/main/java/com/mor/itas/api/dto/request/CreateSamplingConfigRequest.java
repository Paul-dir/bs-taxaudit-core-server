package com.mor.itas.api.dto.request;

import com.mor.itas.domain.model.QaSamplingConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSamplingConfigRequest {
    @NotNull(message = "Sampling method is required")
    private QaSamplingConfig.SamplingMethod samplingMethod;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    private String scopeFilters; // JSON string with filters

    private boolean active = true;

    // Getters and setters
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
}
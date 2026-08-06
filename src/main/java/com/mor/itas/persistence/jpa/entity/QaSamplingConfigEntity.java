package com.mor.itas.persistence.jpa.entity;

import com.mor.itas.domain.model.QaSamplingConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "qa_sampling_configs")
public class QaSamplingConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sampling_method", nullable = false)
    private QaSamplingConfig.SamplingMethod samplingMethod;

    @Column(nullable = false)
    private String frequency;

    @Column(name = "scope_filters")
    private String scopeFilters;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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
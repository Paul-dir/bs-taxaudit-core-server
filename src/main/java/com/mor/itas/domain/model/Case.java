package com.mor.itas.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Case {
    private String caseNumber;
    private String taxpayerName;
    private String taxpayerTIN;
    private CaseStatus status;
    private CaseType type;
    private LocalDate caseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedAuditorId;

    public enum CaseStatus {
        OPEN, UNDER_REVIEW, PENDING_APPROVAL, CLOSED, REOPENED

    }

    public enum CaseType {
        INDIVIDUAL, CORPORATE, PARTNERSHIP
    }

    // Getters and setters
    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getTaxpayerTIN() {
        return taxpayerTIN;
    }

    public void setTaxpayerTIN(String taxpayerTIN) {
        this.taxpayerTIN = taxpayerTIN;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        this.type = type;
    }

    public LocalDate getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(LocalDate caseDate) {
        this.caseDate = caseDate;
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

    public String getAssignedAuditorId() {
        return assignedAuditorId;
    }

    public void setAssignedAuditorId(String assignedAuditorId) {
        this.assignedAuditorId = assignedAuditorId;
    }
}
package com.mor.itas.domain.model.qa;

import java.time.LocalDateTime;

public class FollowUpAction {
    private Long id;
    private Long qaReviewCaseId;
    private String type; // PROCEDURAL_ADJUSTMENT, STAKEHOLDER_NOTIFICATION, DISCIPLINARY_ACTION
    private String description;
    private String assignedTo;
    private String status; // PENDING, DONE
    private String determinedBy;
    private LocalDateTime determinedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQaReviewCaseId() {
        return qaReviewCaseId;
    }

    public void setQaReviewCaseId(Long qaReviewCaseId) {
        this.qaReviewCaseId = qaReviewCaseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeterminedBy() {
        return determinedBy;
    }

    public void setDeterminedBy(String determinedBy) {
        this.determinedBy = determinedBy;
    }

    public LocalDateTime getDeterminedAt() {
        return determinedAt;
    }

    public void setDeterminedAt(LocalDateTime determinedAt) {
        this.determinedAt = determinedAt;
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
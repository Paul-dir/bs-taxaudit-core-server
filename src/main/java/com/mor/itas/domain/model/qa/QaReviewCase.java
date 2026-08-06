package com.mor.itas.domain.model.qa;

import java.time.LocalDateTime;

public class QaReviewCase {
    private Long id;
    private String qaCaseNumber;
    private String originalCaseNumber;
    private QaReviewCaseStatus status;
    private LocalDateTime selectedAt;
    private String selectionBasis;
    private String assignedReviewerId;
    private String assignedTeamLeadId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum QaReviewCaseStatus {
        SELECTED, ASSIGNED, PLAN_SUBMITTED, PLAN_APPROVED, UNDER_REVIEW, REVIEW_SUBMITTED,
        REVIEW_APPROVED, DRAFT_REPORT_GENERATED, REPORT_UNDER_REVIEW, AGENDAS_SUBMITTED,
        AGENDAS_APPROVED, EXIT_CONFERENCE_SCHEDULED, EXIT_CONFERENCE_CONDUCTED,
        REPORT_ADJUSTED, REPORT_FINALIZED, FOLLOW_UP_DETERMINED, FOLLOW_UP_IN_PROGRESS,
        FOLLOW_UP_VERIFIED, CLOSED
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQaCaseNumber() {
        return qaCaseNumber;
    }

    public void setQaCaseNumber(String qaCaseNumber) {
        this.qaCaseNumber = qaCaseNumber;
    }

    public String getOriginalCaseNumber() {
        return originalCaseNumber;
    }

    public void setOriginalCaseNumber(String originalCaseNumber) {
        this.originalCaseNumber = originalCaseNumber;
    }

    public QaReviewCaseStatus getStatus() {
        return status;
    }

    public void setStatus(QaReviewCaseStatus status) {
        this.status = status;
    }

    public LocalDateTime getSelectedAt() {
        return selectedAt;
    }

    public void setSelectedAt(LocalDateTime selectedAt) {
        this.selectedAt = selectedAt;
    }

    public String getSelectionBasis() {
        return selectionBasis;
    }

    public void setSelectionBasis(String selectionBasis) {
        this.selectionBasis = selectionBasis;
    }

    public String getAssignedReviewerId() {
        return assignedReviewerId;
    }

    public void setAssignedReviewerId(String assignedReviewerId) {
        this.assignedReviewerId = assignedReviewerId;
    }

    public String getAssignedTeamLeadId() {
        return assignedTeamLeadId;
    }

    public void setAssignedTeamLeadId(String assignedTeamLeadId) {
        this.assignedTeamLeadId = assignedTeamLeadId;
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
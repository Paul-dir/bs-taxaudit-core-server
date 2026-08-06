package com.mor.itas.persistence.jpa.entity;

import com.mor.itas.domain.model.QaReviewCase;
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
@Table(name = "qa_review_cases")
public class QaReviewCaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qa_case_number", unique = true, nullable = false)
    private String qaCaseNumber;

    @Column(name = "original_case_number", nullable = false)
    private String originalCaseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QaReviewCase.QaReviewCaseStatus status;

    @Column(name = "selected_at", nullable = false)
    private LocalDateTime selectedAt;

    @Column(name = "selection_basis")
    private String selectionBasis;

    @Column(name = "assigned_reviewer_id")
    private String assignedReviewerId;

    @Column(name = "assigned_team_lead_id")
    private String assignedTeamLeadId;

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

    public QaReviewCase.QaReviewCaseStatus getStatus() {
        return status;
    }

    public void setStatus(QaReviewCase.QaReviewCaseStatus status) {
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
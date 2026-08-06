package com.mor.itas.domain.model;

import java.time.LocalDateTime;

public class QaFinding {
    private Long id;
    private Long qaReviewCaseId;
    private String areaCriterionReviewed;
    private String outcome; // COMPLIANT, MINOR_ISSUE, MAJOR_ISSUE, NON_COMPLIANT
    private String evidenceNotes;
    private String recordedBy;
    private LocalDateTime createdAt;

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

    public String getAreaCriterionReviewed() {
        return areaCriterionReviewed;
    }

    public void setAreaCriterionReviewed(String areaCriterionReviewed) {
        this.areaCriterionReviewed = areaCriterionReviewed;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getEvidenceNotes() {
        return evidenceNotes;
    }

    public void setEvidenceNotes(String evidenceNotes) {
        this.evidenceNotes = evidenceNotes;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
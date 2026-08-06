package com.mor.itas.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "qa_findings")
public class QaFindingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qa_review_case_id", nullable = false)
    private Long qaReviewCaseId;

    @Column(name = "area_criterion_reviewed", nullable = false)
    private String areaCriterionReviewed;

    @Column(nullable = false)
    private String outcome;

    @Column(name = "evidence_notes")
    private String evidenceNotes;

    @Column(name = "recorded_by", nullable = false)
    private String recordedBy;

    @Column(nullable = false, updatable = false)
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
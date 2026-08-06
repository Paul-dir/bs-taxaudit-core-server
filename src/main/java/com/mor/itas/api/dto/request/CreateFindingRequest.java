package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFindingRequest {
    @NotNull(message = "QA review case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Area/Criterion reviewed is required")
    private String areaCriterionReviewed;

    @NotBlank(message = "Outcome is required")
    private String outcome; // COMPLIANT, MINOR_ISSUE, MAJOR_ISSUE, NON_COMPLIANT

    private String evidenceNotes;

    @NotBlank(message = "Recorded by is required")
    private String recordedBy;

    // Getters and setters
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
}
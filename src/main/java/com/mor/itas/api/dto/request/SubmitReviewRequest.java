package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmitReviewRequest {
    @NotNull(message = "QA case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Reviewer ID is required")
    private String reviewerId;

    @NotBlank(message = "Review outcome is required")
    private String outcome; // COMPLIANT, NON_COMPLIANT, PARTIALLY_COMPLIANT

    private String comments;

    // Getters and setters
    public Long getQaReviewCaseId() {
        return qaReviewCaseId;
    }

    public void setQaReviewCaseId(Long qaReviewCaseId) {
        this.qaReviewCaseId = qaReviewCaseId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
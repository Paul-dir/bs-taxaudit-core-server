package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GenerateReportRequest {
    @NotNull(message = "QA review case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Summary is required")
    private String summary;

    private String recommendations;

    // Getters and setters
    public Long getQaReviewCaseId() {
        return qaReviewCaseId;
    }

    public void setQaReviewCaseId(Long qaReviewCaseId) {
        this.qaReviewCaseId = qaReviewCaseId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
}
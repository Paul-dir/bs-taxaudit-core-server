package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateActionPlanRequest {
    @NotNull(message = "QA review case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Plan details are required")
    private String planDetails;

    @NotBlank(message = "Prepared by is required")
    private String preparedBy;

    // Getters and setters
    public Long getQaReviewCaseId() {
        return qaReviewCaseId;
    }

    public void setQaReviewCaseId(Long qaReviewCaseId) {
        this.qaReviewCaseId = qaReviewCaseId;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(String planDetails) {
        this.planDetails = planDetails;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }
}
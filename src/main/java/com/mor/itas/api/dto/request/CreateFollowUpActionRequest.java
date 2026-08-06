package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFollowUpActionRequest {
    @NotNull(message = "QA review case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Assigned to is required")
    private String assignedTo;

    public Long getQaReviewCaseId() { return qaReviewCaseId; }
    public void setQaReviewCaseId(Long qaReviewCaseId) { this.qaReviewCaseId = qaReviewCaseId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}
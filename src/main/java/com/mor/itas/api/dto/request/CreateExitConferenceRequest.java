package com.mor.itas.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateExitConferenceRequest {
    @NotNull(message = "QA review case ID is required")
    private Long qaReviewCaseId;

    @NotBlank(message = "Prepared by role is required")
    private String preparedByRole;

    @NotBlank(message = "Agenda items are required")
    private String items;

    @NotNull(message = "Scheduled at is required")
    private LocalDateTime scheduledAt;

    // Getters and setters
    public Long getQaReviewCaseId() {
        return qaReviewCaseId;
    }

    public void setQaReviewCaseId(Long qaReviewCaseId) {
        this.qaReviewCaseId = qaReviewCaseId;
    }

    public String getPreparedByRole() {
        return preparedByRole;
    }

    public void setPreparedByRole(String preparedByRole) {
        this.preparedByRole = preparedByRole;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
package com.mor.itas.domain.model.qa;

import java.time.LocalDateTime;

public class ExitConference {
    private Long id;
    private Long qaReviewCaseId;
    private LocalDateTime scheduledAt;
    private String attendees; // JSON string
    private String minutesOutcomeNotes;
    private LocalDateTime conductedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public String getMinutesOutcomeNotes() {
        return minutesOutcomeNotes;
    }

    public void setMinutesOutcomeNotes(String minutesOutcomeNotes) {
        this.minutesOutcomeNotes = minutesOutcomeNotes;
    }

    public LocalDateTime getConductedAt() {
        return conductedAt;
    }

    public void setConductedAt(LocalDateTime conductedAt) {
        this.conductedAt = conductedAt;
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
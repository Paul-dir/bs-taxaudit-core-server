package com.mor.itas.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "exit_conferences")
public class ExitConferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qa_review_case_id", nullable = false)
    private Long qaReviewCaseId;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column
    private String attendees;

    @Column(name = "minutes_outcome_notes")
    private String minutesOutcomeNotes;

    @Column(name = "conducted_at")
    private LocalDateTime conductedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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
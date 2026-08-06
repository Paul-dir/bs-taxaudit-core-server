package com.mor.itas.domain.model;

import java.time.LocalDateTime;

public class FollowUpVerification {
    private Long id;
    private Long followUpActionId;
    private String verifiedBy;
    private LocalDateTime verifiedAt;
    private String outcome; // ADDRESSED, NOT_ADDRESSED
    private String comments;
    private LocalDateTime createdAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowUpActionId() {
        return followUpActionId;
    }

    public void setFollowUpActionId(Long followUpActionId) {
        this.followUpActionId = followUpActionId;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
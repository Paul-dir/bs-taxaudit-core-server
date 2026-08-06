package com.mor.itas.domain.model;

import com.mor.itas.domain.aggregate.AggregateRoot;
import com.mor.itas.domain.exception.DomainException;
import com.mor.itas.domain.valueobject.FindingOutcome;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * QA Finding aggregate root.
 * Represents a specific non-conformity or observation identified during QA review.
 */
public class QaFinding extends AggregateRoot {

    private final UUID id;
    private final UUID reviewCaseId;
    private String description;
    private String severity;
    private FindingOutcome outcome;
    private String correctiveAction;
    private final Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private QaFinding(UUID id, UUID reviewCaseId, String description, String severity) {
        this.id = id;
        this.reviewCaseId = reviewCaseId;
        this.description = description;
        this.severity = severity;
        this.outcome = FindingOutcome.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static QaFinding create(UUID reviewCaseId, String description, String severity) {
        Objects.requireNonNull(reviewCaseId, "reviewCaseId");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(severity, "severity");

        return new QaFinding(UUID.randomUUID(), reviewCaseId, description, severity);
    }

    public void assess(FindingOutcome outcome, String correctiveAction) {
        if (outcome == null) {
            throw new DomainException("outcome cannot be null");
        }
        this.outcome = outcome;
        this.correctiveAction = correctiveAction;
        touch();
    }

    private void touch() { this.updatedAt = Instant.now(); }

    @Override
    public UUID getId() { return id; }
    public UUID getReviewCaseId() { return reviewCaseId; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public FindingOutcome getOutcome() { return outcome; }
    public String getCorrectiveAction() { return correctiveAction; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    public static QaFinding rehydrate(UUID id, UUID reviewCaseId, String description,
                                       String severity, FindingOutcome outcome,
                                       String correctiveAction, Instant createdAt,
                                       Instant updatedAt, Long version) {
        QaFinding finding = new QaFinding(id, reviewCaseId, description, severity);
        finding.outcome = outcome;
        finding.correctiveAction = correctiveAction;
        finding.updatedAt = updatedAt;
        finding.version = version;
        finding.pullEvents();
        return finding;
    }
}
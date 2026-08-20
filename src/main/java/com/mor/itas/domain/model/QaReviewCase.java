package com.mor.itas.domain.model;

import com.mor.itas.domain.aggregate.AggregateRoot;
import com.mor.itas.domain.event.QaCaseSampledEvent;
import com.mor.itas.domain.event.ReviewSubmittedEvent;
import com.mor.itas.domain.exception.DomainException;
import com.mor.itas.domain.valueobject.QaReviewCaseStatus;
import com.mor.itas.domain.valueobject.SamplingMethod;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * QA Review Case aggregate root.
 *
 * Lifecycle:
 *   DRAFT → UNDER_REVIEW → FINDINGS_RECORDED → REPORT_GENERATED →
 *   APPROVED → CLOSED
 *   Or: REPORT_GENERATED → REJECTED → UNDER_REVIEW (rework loop)
 *
 * Invariants:
 *   - State transitions are guarded — invalid transitions throw InvalidTransitionException.
 *   - Findings can only be added in UNDER_REVIEW state.
 *   - Report can only be generated in FINDINGS_RECORDED state.
 */
public class QaReviewCase extends AggregateRoot {

    private final UUID id;
    private final UUID caseId;
    private final UUID assignedTo;
    private QaReviewCaseStatus status;
    private final List<UUID> findingIds;
    private UUID reportId;
    private final SamplingMethod samplingMethod;
    private final Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private QaReviewCase(UUID id, UUID caseId, UUID assignedTo, SamplingMethod samplingMethod) {
        this.id = id;
        this.caseId = caseId;
        this.assignedTo = assignedTo;
        this.samplingMethod = samplingMethod;
        this.status = QaReviewCaseStatus.DRAFT;
        this.findingIds = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static QaReviewCase open(UUID caseId, UUID assignedTo, SamplingMethod samplingMethod) {
        Objects.requireNonNull(caseId, "caseId");
        Objects.requireNonNull(assignedTo, "assignedTo");
        Objects.requireNonNull(samplingMethod, "samplingMethod");

        QaReviewCase reviewCase = new QaReviewCase(UUID.randomUUID(), caseId, assignedTo, samplingMethod);
        reviewCase.registerEvent(new QaCaseSampledEvent(
            UUID.randomUUID(), Instant.now(), caseId, reviewCase.getId(), samplingMethod));
        return reviewCase;
    }

    public void submitReview(String submittedBy) {
        if (status != QaReviewCaseStatus.DRAFT && status != QaReviewCaseStatus.REJECTED) {
            throw new InvalidTransitionException(status, "submitReview");
        }
        status = QaReviewCaseStatus.UNDER_REVIEW;
        touch();
        registerEvent(new ReviewSubmittedEvent(
            UUID.randomUUID(), Instant.now(), id, submittedBy));
    }

    public void recordFinding(UUID findingId) {
        if (status != QaReviewCaseStatus.UNDER_REVIEW) {
            throw new InvalidTransitionException(status, "recordFinding");
        }
        findingIds.add(findingId);
        status = QaReviewCaseStatus.FINDINGS_RECORDED;
        touch();
        registerEvent(new FindingRecordedEvent(
            UUID.randomUUID(), Instant.now(), id, findingId, 
            com.mor.itas.domain.valueobject.FindingOutcome.PENDING));
    }

    public void generateReport(UUID reportId) {
        if (status != QaReviewCaseStatus.FINDINGS_RECORDED) {
            throw new InvalidTransitionException(status, "generateReport");
        }
        this.reportId = reportId;
        status = QaReviewCaseStatus.REPORT_GENERATED;
        touch();
        registerEvent(new ReportGeneratedEvent(
            UUID.randomUUID(), Instant.now(), id, reportId));
    }

    public void approve() {
        if (status != QaReviewCaseStatus.REPORT_GENERATED) {
            throw new InvalidTransitionException(status, "approve");
        }
        status = QaReviewCaseStatus.APPROVED;
        touch();
    }

    public void reject() {
        if (status != QaReviewCaseStatus.REPORT_GENERATED) {
            throw new InvalidTransitionException(status, "reject");
        }
        status = QaReviewCaseStatus.REJECTED;
        touch();
    }

    public void close() {
        if (status != QaReviewCaseStatus.APPROVED) {
            throw new InvalidTransitionException(status, "close");
        }
        status = QaReviewCaseStatus.CLOSED;
        touch();
    }

    private void touch() { this.updatedAt = Instant.now(); }

    @Override
    public UUID getId() { return id; }
    public UUID getCaseId() { return caseId; }
    public UUID getAssignedTo() { return assignedTo; }
    public QaReviewCaseStatus getStatus() { return status; }
    public List<UUID> getFindingIds() { return List.copyOf(findingIds); }
    public Optional<UUID> getReportId() { return Optional.ofNullable(reportId); }
    public SamplingMethod getSamplingMethod() { return samplingMethod; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    public static QaReviewCase rehydrate(UUID id, UUID caseId, UUID assignedTo,
                                          QaReviewCaseStatus status, List<UUID> findingIds,
                                          UUID reportId, SamplingMethod samplingMethod,
                                          Instant createdAt, Instant updatedAt, Long version) {
        QaReviewCase reviewCase = new QaReviewCase(id, caseId, assignedTo, samplingMethod);
        reviewCase.status = status;
        reviewCase.findingIds.clear();
        reviewCase.findingIds.addAll(findingIds);
        reviewCase.reportId = reportId;
        reviewCase.updatedAt = updatedAt;
        reviewCase.version = version;
        reviewCase.pullEvents();
        return reviewCase;
    }

    public static class InvalidTransitionException extends DomainException {
        public InvalidTransitionException(QaReviewCaseStatus status, String op) {
            super("cannot " + op + " in status " + status);
        }
    }
}
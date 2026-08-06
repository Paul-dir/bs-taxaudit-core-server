package com.mor.itas.domain.event;

import com.mor.itas.domain.valueobject.FindingOutcome;

import java.time.Instant;
import java.util.UUID;

/**
 * Event raised when a finding is recorded in a QA review.
 */
public record FindingRecordedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID reviewCaseId,
    UUID findingId,
    FindingOutcome outcome
) implements DomainEvent {
}
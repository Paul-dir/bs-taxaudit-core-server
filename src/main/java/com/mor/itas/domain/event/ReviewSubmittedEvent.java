package com.mor.itas.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Event raised when a QA review is submitted.
 */
public record ReviewSubmittedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID reviewCaseId,
    String submittedBy
) implements DomainEvent {
}
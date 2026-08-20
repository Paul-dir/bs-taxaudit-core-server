package com.mor.itas.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Event raised when a QA report is generated.
 */
public record ReportGeneratedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID reviewCaseId,
    UUID reportId
) implements DomainEvent {
}
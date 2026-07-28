package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a draft audit report is submitted for review.
 */
public record DraftAuditReportSubmittedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String preparedByActorId
) implements DomainEvent {

    public static DraftAuditReportSubmittedEvent of(UUID deskAuditId, String preparedByActorId) {
        return new DraftAuditReportSubmittedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            preparedByActorId
        );
    }
}
package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when findings are recorded for a desk audit.
 */
public record DeskAuditFindingsRecordedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    int findingsCount
) implements DomainEvent {

    public static DeskAuditFindingsRecordedEvent of(UUID deskAuditId, int findingsCount) {
        return new DeskAuditFindingsRecordedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            findingsCount
        );
    }
}
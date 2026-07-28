package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a desk audit is started.
 */
public record DeskAuditStartedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    UUID auditCaseId,
    String tin
) implements DomainEvent {

    public static DeskAuditStartedEvent of(UUID deskAuditId, UUID auditCaseId, String tin) {
        return new DeskAuditStartedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            auditCaseId,
            tin
        );
    }
}
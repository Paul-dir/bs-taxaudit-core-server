package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when desk audit is escalated to comprehensive audit.
 */
public record DeskAuditEscalatedToComprehensiveEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    UUID auditCaseId,
    String escalatedByActorId
) implements DomainEvent {

    public static DeskAuditEscalatedToComprehensiveEvent of(UUID deskAuditId, UUID auditCaseId, String escalatedByActorId) {
        return new DeskAuditEscalatedToComprehensiveEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            auditCaseId,
            escalatedByActorId
        );
    }
}
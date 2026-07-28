package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a desk audit is finalized.
 */
public record DeskAuditFinalizedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String finalizedByActorId
) implements DomainEvent {

    public static DeskAuditFinalizedEvent of(UUID deskAuditId, String finalizedByActorId) {
        return new DeskAuditFinalizedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            finalizedByActorId
        );
    }
}
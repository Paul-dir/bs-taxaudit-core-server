package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a fraud indicator is flagged during desk audit.
 */
public record FraudIndicatorFlaggedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String tin,
    String indicatorNotes,
    String flaggedByActorId
) implements DomainEvent {

    public static FraudIndicatorFlaggedEvent of(UUID deskAuditId, String tin, String indicatorNotes, String flaggedByActorId) {
        return new FraudIndicatorFlaggedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            tin,
            indicatorNotes,
            flaggedByActorId
        );
    }
}
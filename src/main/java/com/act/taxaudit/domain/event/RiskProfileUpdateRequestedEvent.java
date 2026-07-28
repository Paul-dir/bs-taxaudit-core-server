package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when risk profile update is requested.
 */
public record RiskProfileUpdateRequestedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String tin,
    String justification
) implements DomainEvent {

    public static RiskProfileUpdateRequestedEvent of(UUID deskAuditId, String tin, String justification) {
        return new RiskProfileUpdateRequestedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            tin,
            justification
        );
    }
}
package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when team leader makes a decision on draft report.
 */
public record TeamLeaderDeskAuditDecidedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String decision,
    String actorId,
    String narrative
) implements DomainEvent {

    public static TeamLeaderDeskAuditDecidedEvent of(UUID deskAuditId, String decision, String actorId, String narrative) {
        return new TeamLeaderDeskAuditDecidedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            decision,
            actorId,
            narrative
        );
    }
}
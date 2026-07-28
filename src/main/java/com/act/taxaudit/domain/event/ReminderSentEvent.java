package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when a reminder is sent for overdue document requests.
 */
public record ReminderSentEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String tin,
    String templateCode,
    Instant nextReminderAt
) implements DomainEvent {

    public static ReminderSentEvent of(UUID deskAuditId, String tin, String templateCode, Instant nextReminderAt) {
        return new ReminderSentEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            tin,
            templateCode,
            nextReminderAt
        );
    }
}
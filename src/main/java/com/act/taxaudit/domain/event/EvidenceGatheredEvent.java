package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when evidence is gathered for a desk audit.
 */
public record EvidenceGatheredEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String evidenceId,
    String evidenceSourceType
) implements DomainEvent {

    public static EvidenceGatheredEvent of(UUID deskAuditId, String evidenceId, String evidenceSourceType) {
        return new EvidenceGatheredEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            evidenceId,
            evidenceSourceType
        );
    }
}
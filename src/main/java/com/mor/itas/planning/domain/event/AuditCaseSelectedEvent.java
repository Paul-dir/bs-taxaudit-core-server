package com.mor.itas.planning.domain.event;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AuditCaseSelectedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant occurredAt = Instant.now();
    UUID caseId;
    String tin;

    @Override
    public UUID getAggregateId() {
        return caseId;
    }

    @Override
    public EventType getEventType() {
        return EventType.AUDIT_CASE_SELECTED;
    }
}

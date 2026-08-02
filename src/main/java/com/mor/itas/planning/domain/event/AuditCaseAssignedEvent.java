package com.mor.itas.planning.domain.event;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AuditCaseAssignedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant occurredAt = Instant.now();
    UUID caseId;
    UUID assignedAuditorId;

    @Override
    public UUID getAggregateId() {
        return caseId;
    }

    @Override
    public EventType getEventType() {
        return EventType.AUDIT_CASE_ASSIGNED;
    }
}

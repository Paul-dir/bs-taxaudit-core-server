package com.mor.itas.planning.domain.event;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AnnualAuditPlanCreatedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant occurredAt = Instant.now();
    UUID planId;
    int year;
    int totalCasesPlanned;
    UUID directorId;

    @Override
    public UUID getAggregateId() {
        return planId;
    }

    @Override
    public EventType getEventType() {
        return EventType.ANNUAL_AUDIT_PLAN_CREATED;
    }
}

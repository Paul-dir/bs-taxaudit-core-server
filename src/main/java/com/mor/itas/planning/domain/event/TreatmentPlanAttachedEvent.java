package com.mor.itas.planning.domain.event;

import com.itas.bs.taxaudit.domain.valueobject.TreatmentPlanType;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class TreatmentPlanAttachedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant occurredAt = Instant.now();
    UUID caseId;
    TreatmentPlanType planType;

    @Override
    public UUID getAggregateId() {
        return caseId;
    }

    @Override
    public EventType getEventType() {
        return EventType.TREATMENT_PLAN_ATTACHED;
    }
}

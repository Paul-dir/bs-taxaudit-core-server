package com.mor.itas.planning.domain.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();
    Instant getOccurredAt();
    UUID getAggregateId();
    EventType getEventType();
}

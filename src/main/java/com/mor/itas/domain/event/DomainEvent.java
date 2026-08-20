package com.mor.itas.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Marker interface for all domain events. Implementations are Java records.
 */
public interface DomainEvent {
    UUID eventId();
    Instant occurredAt();
}
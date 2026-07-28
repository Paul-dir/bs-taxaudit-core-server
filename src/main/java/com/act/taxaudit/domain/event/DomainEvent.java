package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all domain events in the tax audit system.
 */
public interface DomainEvent {
    UUID eventId();
    Instant occurredAt();
}

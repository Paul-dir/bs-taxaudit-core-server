package com.mor.itas.domain.aggregate;

import com.mor.itas.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Base class for all aggregate roots.
 * <p>
 * Identity:
 *   Subclasses must expose their own UUID id via {@link #getId()}. Equality and hashCode
 *   are based on aggregate identity (id + concrete class), per Eric Evans' DDD entity rule.
 * <p>
 * Domain events:
 *   Aggregates record events via {@link #registerEvent}. Use cases drain them via
 *   {@link #pullEvents()} after save() and dispatch them via the outbox or event publisher.
 *   Events are NEVER published from inside an aggregate.
 */
public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * The aggregate's identity. Subclasses implement this (typically via @Getter on a UUID id field).
     * Must never return null after construction.
     */
    public abstract UUID getId();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
    }

    /**
     * Returns all pending domain events and clears the internal list.
     * Must be called by the use case after repository.save(), never inside the aggregate.
     */
    public List<DomainEvent> pullEvents() {
        List<DomainEvent> events = Collections.unmodifiableList(new ArrayList<>(domainEvents));
        domainEvents.clear();
        return events;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateRoot other)) return false;
        if (!getClass().equals(other.getClass())) return false;
        UUID myId = getId();
        UUID otherId = other.getId();
        return myId != null && myId.equals(otherId);
    }

    @Override
    public final int hashCode() {
        UUID id = getId();
        return id == null ? 0 : id.hashCode();
    }
}
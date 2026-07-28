package com.act.taxaudit.domain.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all aggregates in the tax audit domain.
 * Follows the same pattern as bs-filling-core-server:
 * - Aggregates register domain events internally
 * - Events are drained via pullEvents() after repository.save()
 * - Events are never published from inside the aggregate
 */
public abstract class AggregateRoot {

    private final List<Object> domainEvents = new ArrayList<>();

    protected void registerEvent(Object event) {
        domainEvents.add(event);
    }

    public List<Object> pullEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public List<Object> getUncommittedEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
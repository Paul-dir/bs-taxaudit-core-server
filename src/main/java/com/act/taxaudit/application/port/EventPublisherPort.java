package com.act.taxaudit.application.port;

/**
 * Port for publishing domain events.
 */
public interface EventPublisherPort {
    void publish(Object event);
}
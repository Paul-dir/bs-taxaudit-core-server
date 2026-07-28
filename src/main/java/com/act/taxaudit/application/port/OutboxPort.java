package com.act.taxaudit.application.port;

/**
 * Port for writing outbox entries (transactional outbox pattern).
 */
public interface OutboxPort {
    void save(String aggregateType, String aggregateId, String eventType, String payload);
}
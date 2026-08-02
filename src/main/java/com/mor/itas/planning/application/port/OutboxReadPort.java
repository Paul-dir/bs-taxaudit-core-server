package com.mor.itas.planning.application.port;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxReadPort {
    List<OutboxEventData> findUnpublishedEvents(int limit);
    void markAsPublished(UUID eventId);

    @Value
    @Builder
    class OutboxEventData {
        UUID id;
        String aggregateType;
        UUID aggregateId;
        String eventType;
        String payload;
        Instant createdAt;
    }
}

package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when sampling method is determined for a desk audit.
 */
public record SamplingMethodSelectedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String samplingMethod,
    int sampleSize
) implements DomainEvent {

    public static SamplingMethodSelectedEvent of(UUID deskAuditId, String samplingMethod, int sampleSize) {
        return new SamplingMethodSelectedEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            samplingMethod,
            sampleSize
        );
    }
}
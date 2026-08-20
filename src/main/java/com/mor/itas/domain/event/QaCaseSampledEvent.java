package com.mor.itas.domain.event;

import com.mor.itas.domain.valueobject.SamplingMethod;

import java.time.Instant;
import java.util.UUID;

/**
 * Event raised when a case is selected for QA review via sampling.
 */
public record QaCaseSampledEvent(
    UUID eventId,
    Instant occurredAt,
    UUID caseId,
    UUID reviewCaseId,
    SamplingMethod samplingMethod
) implements DomainEvent {
}
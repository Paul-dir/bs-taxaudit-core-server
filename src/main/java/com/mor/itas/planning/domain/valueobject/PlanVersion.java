package com.mor.itas.planning.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class PlanVersion {
    int versionNumber;
    String changeSummary;
    UUID changedBy;
    Instant changedAt;
    String snapshot;
}

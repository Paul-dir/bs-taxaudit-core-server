package com.mor.itas.planning.application.port;

import lombok.Value;

import java.util.UUID;

public interface WorkforceEnginePort {
    CapacityCheckResult checkCapacity(UUID auditorId, String caseComplexity);

    @Value
    class CapacityCheckResult {
        boolean eligible;
        int activeCases;
        int maxCapacity;
    }
}

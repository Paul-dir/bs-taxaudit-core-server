package com.mor.itas.planning.engineadapter.workforce;

import com.itas.bs.taxaudit.application.port.WorkforceEnginePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkforceEngineAdapter implements WorkforceEnginePort {
    @Override
    public CapacityCheckResult checkCapacity(UUID auditorId, String caseComplexity) {
        return new CapacityCheckResult(true, 2, 5); // Stub implementation
    }
}

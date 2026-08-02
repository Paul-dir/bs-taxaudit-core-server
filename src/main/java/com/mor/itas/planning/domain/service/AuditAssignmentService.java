package com.mor.itas.planning.domain.service;

import com.itas.bs.taxaudit.application.port.RuleEnginePort;
import com.itas.bs.taxaudit.application.port.WorkforceEnginePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditAssignmentService {
    private final RuleEnginePort ruleEnginePort;
    private final WorkforceEnginePort workforceEnginePort;

    public UUID suggestAuditor(UUID caseId, String complexity, List<String> requirements) {
        List<UUID> matchedAuditors = ruleEnginePort.matchAuditors(caseId, requirements);
        for (UUID auditorId : matchedAuditors) {
            WorkforceEnginePort.CapacityCheckResult capacity = workforceEnginePort.checkCapacity(auditorId, complexity);
            if (capacity.isEligible() && capacity.getActiveCases() < capacity.getMaxCapacity()) {
                return auditorId; // Return first matching auditor who has capacity
            }
        }
        return null; // Return null if no auditor matches with available capacity
    }
}

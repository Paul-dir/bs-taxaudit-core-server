package com.mor.itas.planning.engineadapter.rule;

import com.itas.bs.taxaudit.application.port.RuleEnginePort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class RuleEngineAdapter implements RuleEnginePort {
    @Override
    public List<UUID> matchAuditors(UUID caseId, List<String> requirements) {
        // External call to Rule Engine for auditor matching
        return Collections.emptyList();
    }
}

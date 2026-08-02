package com.mor.itas.planning.application.port;

import java.util.List;
import java.util.UUID;

public interface RuleEnginePort {
    List<UUID> matchAuditors(UUID caseId, List<String> requirements);
}

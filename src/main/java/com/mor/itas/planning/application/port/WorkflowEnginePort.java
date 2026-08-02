package com.mor.itas.planning.application.port;

import java.util.UUID;

public interface WorkflowEnginePort {
    UUID startApprovalChain(UUID entityId, String chainType);
    void submitDecision(UUID instanceId, String decision, String comment);
}

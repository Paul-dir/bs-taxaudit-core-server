package com.mor.itas.planning.engineadapter.workflow;

import com.itas.bs.taxaudit.application.port.WorkflowEnginePort;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Component
@Slf4j
public class WorkflowEngineAdapter implements WorkflowEnginePort {
    @Override
    public UUID startApprovalChain(UUID entityId, String chainType) {
        log.info("Starting workflow chain {} for entity {}", chainType, entityId);
        return UUID.randomUUID();
    }

    @Override
    public void submitDecision(UUID instanceId, String decision, String comment) {
        log.info("Submitting decision {} for workflow instance {}", decision, instanceId);
    }
}

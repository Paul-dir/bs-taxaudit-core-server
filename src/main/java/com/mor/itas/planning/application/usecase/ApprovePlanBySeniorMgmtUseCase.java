package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AnnualAuditPlanRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.application.port.WorkflowEnginePort;
import com.itas.bs.taxaudit.domain.event.AnnualAuditPlanApprovedEvent;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovePlanBySeniorMgmtUseCase {
    private final AnnualAuditPlanRepositoryPort planRepository;
    private final WorkflowEnginePort workflowEnginePort;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID planId, UUID actorId, UUID workflowInstanceId, String comments) {
        AnnualAuditPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new DomainException("Annual Audit Plan not found"));

        plan.approveBySeniorManagement(actorId);

        AnnualAuditPlan savedPlan = planRepository.save(plan);
        workflowEnginePort.submitDecision(workflowInstanceId, "APPROVED", comments);

        AnnualAuditPlanApprovedEvent event = new AnnualAuditPlanApprovedEvent(
                savedPlan.getId(), savedPlan.getYear(), actorId
        );
        eventDispatchPort.dispatch(event);
    }
}

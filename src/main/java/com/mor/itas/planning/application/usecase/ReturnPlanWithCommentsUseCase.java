package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AnnualAuditPlanRepositoryPort;
import com.itas.bs.taxaudit.application.port.WorkflowEnginePort;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReturnPlanWithCommentsUseCase {
    private final AnnualAuditPlanRepositoryPort planRepository;
    private final WorkflowEnginePort workflowEnginePort;

    @Transactional
    public void execute(UUID planId, UUID actorId, UUID workflowInstanceId, String comments) {
        AnnualAuditPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new DomainException("Annual Audit Plan not found"));

        // Using rejectBySeniorManagement since it returns it to director/officer
        plan.rejectBySeniorManagement(actorId);

        planRepository.save(plan);
        workflowEnginePort.submitDecision(workflowInstanceId, "RETURNED", comments);
    }
}

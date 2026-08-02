package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.domain.event.TreatmentPlanAttachedEvent;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.domain.valueobject.TreatmentPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachTreatmentPlanUseCase {
    private final AuditCaseRepositoryPort caseRepository;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID caseId, TreatmentPlan plan) {
        AuditCase auditCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new DomainException("Audit Case not found"));

        auditCase.selectForAudit(plan);
        caseRepository.save(auditCase);

        TreatmentPlanAttachedEvent event = new TreatmentPlanAttachedEvent(
                auditCase.getId(), plan.getPlanType()
        );
        eventDispatchPort.dispatch(event);
    }
}

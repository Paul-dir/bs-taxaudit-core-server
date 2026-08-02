package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.domain.event.AuditCaseReassignedEvent;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReassignAuditCaseUseCase {
    private final AuditCaseRepositoryPort caseRepository;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID caseId, UUID newAuditorId) {
        AuditCase auditCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new DomainException("Audit Case not found"));

        UUID previousAuditorId = auditCase.getAssignedAuditorId();
        
        auditCase.reassignAuditor(newAuditorId);
        caseRepository.save(auditCase);

        AuditCaseReassignedEvent event = new AuditCaseReassignedEvent(
                auditCase.getId(), newAuditorId, previousAuditorId
        );
        eventDispatchPort.dispatch(event);
    }
}

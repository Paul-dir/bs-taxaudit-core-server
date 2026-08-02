package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.domain.event.AuditCaseAssignedEvent;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.domain.service.AuditAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignAuditCaseUseCase {
    private final AuditCaseRepositoryPort caseRepository;
    private final AuditAssignmentService assignmentService;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID caseId, UUID teamLeaderId, UUID requestedAuditorId, List<String> requiredSkills) {
        AuditCase auditCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new DomainException("Audit Case not found"));

        UUID assignedAuditorId = requestedAuditorId;

        // Auto-allocate if manual assignment not provided
        if (assignedAuditorId == null) {
            String complexity = auditCase.getTreatmentPlan() != null 
                    ? auditCase.getTreatmentPlan().getComplexityRating().name() 
                    : "MEDIUM";
            
            assignedAuditorId = assignmentService.suggestAuditor(caseId, complexity, requiredSkills);
            if (assignedAuditorId == null) {
                throw new DomainException("No eligible auditor found with available capacity for this case.");
            }
        }

        auditCase.assignAuditor(assignedAuditorId, teamLeaderId);
        caseRepository.save(auditCase);

        AuditCaseAssignedEvent event = new AuditCaseAssignedEvent(auditCase.getId(), assignedAuditorId);
        eventDispatchPort.dispatch(event);
    }
}

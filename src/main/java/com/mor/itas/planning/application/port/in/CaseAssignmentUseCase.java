package com.mor.itas.planning.application.port.in;

import com.itas.bs.taxaudit.api.dto.request.DistributeCasesRequest;
import com.itas.bs.taxaudit.domain.model.AuditCase;

import java.util.List;
import java.util.UUID;

public interface CaseAssignmentUseCase {
    List<AuditCase> prioritizeAndStoreCases(List<UUID> caseIds, UUID managerId);
    List<AuditCase> distributeToTeamLeaders(DistributeCasesRequest request, UUID managerId);
    List<AuditCase> distributeToAuditors(DistributeCasesRequest request, UUID teamLeaderId);
}

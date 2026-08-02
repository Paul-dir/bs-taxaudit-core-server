package com.mor.itas.planning.api.controller;

import com.itas.bs.taxaudit.api.dto.request.DistributeCasesRequest;
import com.itas.bs.taxaudit.application.port.in.CaseAssignmentUseCase;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cases")
@RequiredArgsConstructor
public class CaseAssignmentController {

    private final CaseAssignmentUseCase caseAssignmentUseCase;

    @PostMapping("/prioritize-store")
    public ResponseEntity<List<AuditCase>> prioritizeAndStore(
            @RequestBody List<UUID> caseIds,
            @RequestHeader("X-User-Id") UUID managerId) {
        return ResponseEntity.ok(caseAssignmentUseCase.prioritizeAndStoreCases(caseIds, managerId));
    }

    @PostMapping("/distribute/team-leaders")
    public ResponseEntity<List<AuditCase>> distributeToTeamLeaders(
            @RequestBody DistributeCasesRequest request,
            @RequestHeader("X-User-Id") UUID managerId) {
        return ResponseEntity.ok(caseAssignmentUseCase.distributeToTeamLeaders(request, managerId));
    }

    @PostMapping("/distribute/auditors")
    public ResponseEntity<List<AuditCase>> distributeToAuditors(
            @RequestBody DistributeCasesRequest request,
            @RequestHeader("X-User-Id") UUID teamLeaderId) {
        return ResponseEntity.ok(caseAssignmentUseCase.distributeToAuditors(request, teamLeaderId));
    }
}

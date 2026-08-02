package com.mor.itas.planning.api.controller.backoffice;

import com.itas.bs.taxaudit.api.dto.request.AssignAuditCaseRequest;
import com.itas.bs.taxaudit.api.dto.request.AttachTreatmentPlanRequest;
import com.itas.bs.taxaudit.api.dto.request.ReassignAuditCaseRequest;
import com.itas.bs.taxaudit.api.dto.request.SelectAndPrioritizeCasesRequest;
import com.itas.bs.taxaudit.api.dto.response.AuditCaseListResponse;
import com.itas.bs.taxaudit.api.dto.response.AuditCaseResponse;
import com.itas.bs.taxaudit.application.usecase.*;
import com.itas.bs.taxaudit.domain.valueobject.TreatmentPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/backoffice/audit-cases")
@RequiredArgsConstructor
public class AuditCaseController {

    private final SelectAndPrioritizeAuditCasesUseCase selectAndPrioritizeUseCase;
    private final AttachTreatmentPlanUseCase attachTreatmentPlanUseCase;
    private final AssignAuditCaseUseCase assignUseCase;
    private final ReassignAuditCaseUseCase reassignUseCase;
    private final GetAuditCaseUseCase getCaseUseCase;
    private final GetAuditCasesByAuditorUseCase getCasesByAuditorUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<AuditCaseResponse> getAuditCase(@PathVariable UUID id) {
        return ResponseEntity.ok(new AuditCaseResponse(getCaseUseCase.getById(id)));
    }

    @GetMapping("/by-tin/{tin}")
    public ResponseEntity<AuditCaseListResponse> getCasesByTin(@PathVariable String tin) {
        return ResponseEntity.ok(new AuditCaseListResponse(getCaseUseCase.getByTin(tin)));
    }

    @GetMapping("/by-auditor/{auditorId}")
    public ResponseEntity<AuditCaseListResponse> getCasesByAuditor(@PathVariable UUID auditorId) {
        return ResponseEntity.ok(new AuditCaseListResponse(getCasesByAuditorUseCase.getByAuditorId(auditorId)));
    }

    @PostMapping("/select-cases")
    public ResponseEntity<Void> selectAndPrioritize(@RequestBody SelectAndPrioritizeCasesRequest request) {
        selectAndPrioritizeUseCase.execute(
                request.getAnnualPlanId(),
                request.getBranch(),
                request.getSegment(),
                request.getAuditTypeStr(),
                request.getRiskMin(),
                request.getRandomSampleCount()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/treatment-plan")
    public ResponseEntity<Void> attachTreatmentPlan(@PathVariable UUID id, @RequestBody AttachTreatmentPlanRequest request) {
        TreatmentPlan plan = TreatmentPlan.builder()
                .planType(request.getPlanType())
                .recommendedActions(request.getRecommendedActions())
                .targetCompletionDate(request.getTargetCompletionDate())
                .complexityRating(request.getComplexityRating())
                .build();
        attachTreatmentPlanUseCase.execute(id, plan);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Void> assignAuditor(@PathVariable UUID id, @RequestBody AssignAuditCaseRequest request) {
        assignUseCase.execute(id, request.getTeamLeaderId(), request.getRequestedAuditorId(), request.getRequiredSkills());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reassign")
    public ResponseEntity<Void> reassignAuditor(@PathVariable UUID id, @RequestBody ReassignAuditCaseRequest request) {
        reassignUseCase.execute(id, request.getNewAuditorId());
        return ResponseEntity.ok().build();
    }
}

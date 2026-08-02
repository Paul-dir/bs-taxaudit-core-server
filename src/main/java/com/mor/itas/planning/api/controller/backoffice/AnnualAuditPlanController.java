package com.mor.itas.planning.api.controller.backoffice;

import com.itas.bs.taxaudit.api.dto.request.*;
import com.itas.bs.taxaudit.api.dto.response.AnnualAuditPlanResponse;
import com.itas.bs.taxaudit.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/backoffice/annual-audit-plans")
@RequiredArgsConstructor
public class AnnualAuditPlanController {

    private final CreateAnnualAuditPlanUseCase createPlanUseCase;
    private final SubmitPlanToDirectorUseCase submitToDirectorUseCase;
    private final ApprovePlanByDirectorUseCase approveByDirectorUseCase;
    private final ReturnPlanWithCommentsUseCase returnPlanUseCase;
    private final SubmitPlanToSeniorMgmtUseCase submitToSeniorMgmtUseCase;
    private final ApprovePlanBySeniorMgmtUseCase approveBySeniorMgmtUseCase;
    private final CascadeAuditPlanToCasesUseCase cascadePlanUseCase;
    private final GetAnnualAuditPlanUseCase getPlanUseCase;

    @PostMapping
    public ResponseEntity<UUID> createPlan(@RequestBody CreateAnnualAuditPlanRequest request) {
        UUID planId = createPlanUseCase.execute(request.getYear(), request.getTotalCasesPlanned(), request.getDirectorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(planId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnualAuditPlanResponse> getPlan(@PathVariable UUID id) {
        return ResponseEntity.ok(new AnnualAuditPlanResponse(getPlanUseCase.getById(id)));
    }

    @PostMapping("/{id}/submit-to-director")
    public ResponseEntity<Void> submitToDirector(@PathVariable UUID id, @RequestBody SubmitPlanToDirectorRequest request) {
        submitToDirectorUseCase.execute(id, request.getActorId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve-by-director")
    public ResponseEntity<Void> approveByDirector(@PathVariable UUID id, @RequestBody ApprovePlanByDirectorRequest request) {
        approveByDirectorUseCase.execute(id, request.getActorId(), request.getWorkflowInstanceId(), request.getComments());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/return-plan")
    public ResponseEntity<Void> returnPlan(@PathVariable UUID id, @RequestBody ReturnPlanWithCommentsRequest request) {
        returnPlanUseCase.execute(id, request.getActorId(), request.getWorkflowInstanceId(), request.getComments());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/submit-to-senior-mgmt")
    public ResponseEntity<Void> submitToSeniorMgmt(@PathVariable UUID id, @RequestBody SubmitPlanToSeniorMgmtRequest request) {
        submitToSeniorMgmtUseCase.execute(id, request.getActorId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve-by-senior-mgmt")
    public ResponseEntity<Void> approveBySeniorMgmt(@PathVariable UUID id, @RequestBody ApprovePlanBySeniorMgmtRequest request) {
        approveBySeniorMgmtUseCase.execute(id, request.getActorId(), request.getWorkflowInstanceId(), request.getComments());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cascade")
    public ResponseEntity<Void> cascadePlan(@PathVariable UUID id, @RequestBody CascadeAuditPlanRequest request) {
        cascadePlanUseCase.execute(id);
        return ResponseEntity.ok().build();
    }
}

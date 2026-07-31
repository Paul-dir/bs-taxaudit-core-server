package com.act.taxaudit.api.controller.backoffice;

import com.act.taxaudit.api.dto.request.*;
import com.act.taxaudit.api.dto.response.ApiResponse;
import com.act.taxaudit.api.dto.response.DeskAuditResponse;
import com.act.taxaudit.application.usecase.*;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.valueobject.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/desk-audits")
public class DeskAuditController {

    private final StartDeskAuditUseCase startDeskAuditUseCase;
    private final GatherEvidenceUseCase gatherEvidenceUseCase;
    private final RequestSupportingDocumentsUseCase requestSupportingDocumentsUseCase;
    private final DetermineSamplingMethodUseCase determineSamplingMethodUseCase;
    private final RecordDeskAuditFindingsUseCase recordDeskAuditFindingsUseCase;
    private final GetDeskAuditUseCase getDeskAuditUseCase;
    private final SubmitDraftReportUseCase submitDraftReportUseCase;
    private final TeamLeaderDeskAuditUseCase teamLeaderDeskAuditUseCase;
    private final FlagFraudUseCase flagFraudUseCase;
    private final FinalizeDeskAuditUseCase finalizeDeskAuditUseCase;

    public DeskAuditController(StartDeskAuditUseCase startDeskAuditUseCase,
                               GatherEvidenceUseCase gatherEvidenceUseCase,
                               RequestSupportingDocumentsUseCase requestSupportingDocumentsUseCase,
                               DetermineSamplingMethodUseCase determineSamplingMethodUseCase,
                               RecordDeskAuditFindingsUseCase recordDeskAuditFindingsUseCase,
                               GetDeskAuditUseCase getDeskAuditUseCase,
                               SubmitDraftReportUseCase submitDraftReportUseCase,
                               TeamLeaderDeskAuditUseCase teamLeaderDeskAuditUseCase,
                               FlagFraudUseCase flagFraudUseCase,
                               FinalizeDeskAuditUseCase finalizeDeskAuditUseCase) {
        this.startDeskAuditUseCase = startDeskAuditUseCase;
        this.gatherEvidenceUseCase = gatherEvidenceUseCase;
        this.requestSupportingDocumentsUseCase = requestSupportingDocumentsUseCase;
        this.determineSamplingMethodUseCase = determineSamplingMethodUseCase;
        this.recordDeskAuditFindingsUseCase = recordDeskAuditFindingsUseCase;
        this.getDeskAuditUseCase = getDeskAuditUseCase;
        this.submitDraftReportUseCase = submitDraftReportUseCase;
        this.teamLeaderDeskAuditUseCase = teamLeaderDeskAuditUseCase;
        this.flagFraudUseCase = flagFraudUseCase;
        this.finalizeDeskAuditUseCase = finalizeDeskAuditUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeskAuditResponse>> startDeskAudit(@RequestBody StartDeskAuditRequest request) {
        DeskAudit deskAudit = startDeskAuditUseCase.execute(request.auditCaseId(), request.tin());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/evidence")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> gatherEvidence(@PathVariable UUID id, @RequestBody GatherEvidenceRequest request) {
        DeskAudit deskAudit = gatherEvidenceUseCase.execute(id, request.actorId());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/document-requests")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> requestDocuments(@PathVariable UUID id, @RequestBody RequestDocumentsRequest request) {
        DeskAudit deskAudit = requestSupportingDocumentsUseCase.execute(id, request.documentTypes(), request.requestedByActorId());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/sampling")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> determineSampling(@PathVariable UUID id, @RequestBody DetermineSamplingRequest request) {
        SampleSelection selection = new SampleSelection(
            request.method(), request.criteria(), request.selectedItems(), request.sampleSize()
        );
        DeskAudit deskAudit = determineSamplingMethodUseCase.execute(id, request.method(), selection);
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/findings")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> recordFindings(@PathVariable UUID id, @RequestBody RecordFindingsRequest request) {
        DeskAudit deskAudit = recordDeskAuditFindingsUseCase.execute(id, request.findings());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/draft-report")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> submitDraftReport(@PathVariable UUID id, @RequestBody SubmitDraftReportRequest request) {
        DeskAudit deskAudit = submitDraftReportUseCase.execute(id, request.toReport(), request.preparedByActorId());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/team-leader-decision")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> teamLeaderDecision(@PathVariable UUID id, @RequestBody TeamLeaderDecisionRequest request) {
        DeskAudit deskAudit = teamLeaderDeskAuditUseCase.execute(id, request.decision(), request.actorId(), request.narrative());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/fraud-flag")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> flagFraud(@PathVariable UUID id, @RequestBody FlagFraudRequest request) {
        DeskAudit deskAudit = flagFraudUseCase.execute(id, request.indicatorNotes(), request.flaggedByActorId());
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> finalizeDeskAudit(@PathVariable UUID id, @RequestBody String finalizedByActorId) {
        DeskAudit deskAudit = finalizeDeskAuditUseCase.execute(id, finalizedByActorId);
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeskAuditResponse>> getDeskAudit(@PathVariable UUID id) {
        DeskAudit deskAudit = getDeskAuditUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(DeskAuditResponse.from(deskAudit)));
    }
}
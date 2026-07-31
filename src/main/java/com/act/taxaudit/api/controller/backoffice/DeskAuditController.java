package com.act.taxaudit.api.controller.backoffice;

import com.act.taxaudit.api.dto.DeskAuditDTOs;
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

    public DeskAuditController(StartDeskAuditUseCase startDeskAuditUseCase,
                               GatherEvidenceUseCase gatherEvidenceUseCase,
                               RequestSupportingDocumentsUseCase requestSupportingDocumentsUseCase,
                               DetermineSamplingMethodUseCase determineSamplingMethodUseCase,
                               RecordDeskAuditFindingsUseCase recordDeskAuditFindingsUseCase,
                               GetDeskAuditUseCase getDeskAuditUseCase) {
        this.startDeskAuditUseCase = startDeskAuditUseCase;
        this.gatherEvidenceUseCase = gatherEvidenceUseCase;
        this.requestSupportingDocumentsUseCase = requestSupportingDocumentsUseCase;
        this.determineSamplingMethodUseCase = determineSamplingMethodUseCase;
        this.recordDeskAuditFindingsUseCase = recordDeskAuditFindingsUseCase;
        this.getDeskAuditUseCase = getDeskAuditUseCase;
    }

    @PostMapping
    public ResponseEntity<DeskAudit> startDeskAudit(@RequestBody DeskAuditDTOs.StartDeskAuditRequest request) {
        DeskAudit deskAudit = startDeskAuditUseCase.execute(request.auditCaseId(), request.tin());
        return ResponseEntity.status(HttpStatus.CREATED).body(deskAudit);
    }

    @PostMapping("/{id}/evidence")
    public ResponseEntity<DeskAudit> gatherEvidence(@PathVariable UUID id, @RequestBody DeskAuditDTOs.GatherEvidenceRequest request) {
        DeskAudit deskAudit = gatherEvidenceUseCase.execute(id, request.actorId());
        return ResponseEntity.ok(deskAudit);
    }

    @PostMapping("/{id}/document-requests")
    public ResponseEntity<DeskAudit> requestDocuments(@PathVariable UUID id, @RequestBody DeskAuditDTOs.RequestDocumentsRequest request) {
        DeskAudit deskAudit = requestSupportingDocumentsUseCase.execute(id, request.documentTypes(), request.requestedByActorId());
        return ResponseEntity.ok(deskAudit);
    }

    @PostMapping("/{id}/sampling")
    public ResponseEntity<DeskAudit> determineSampling(@PathVariable UUID id, @RequestBody DeskAuditDTOs.DetermineSamplingRequest request) {
        SampleSelection selection = new SampleSelection(
            request.method(),
            request.criteria(),
            request.selectedItems(),
            request.sampleSize()
        );
        DeskAudit deskAudit = determineSamplingMethodUseCase.execute(id, request.method(), selection);
        return ResponseEntity.ok(deskAudit);
    }

    @PostMapping("/{id}/findings")
    public ResponseEntity<DeskAudit> recordFindings(@PathVariable UUID id, @RequestBody DeskAuditDTOs.RecordFindingsRequest request) {
        DeskAudit deskAudit = recordDeskAuditFindingsUseCase.execute(id, request.findings());
        return ResponseEntity.ok(deskAudit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeskAudit> getDeskAudit(@PathVariable UUID id) {
        DeskAudit deskAudit = getDeskAuditUseCase.execute(id);
        return ResponseEntity.ok(deskAudit);
    }
}

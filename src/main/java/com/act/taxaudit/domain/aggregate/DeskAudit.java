package com.act.taxaudit.domain.aggregate;

import com.act.taxaudit.domain.exception.DomainException;
import com.act.taxaudit.domain.event.*;
import com.act.taxaudit.domain.model.EvidenceItem;
import com.act.taxaudit.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DeskAudit aggregate root - the central entity for desk audit execution.
 * Implements the state machine: STARTED → EVIDENCE_GATHERING → DOCUMENTS_REQUESTED →
 * SAMPLING_DETERMINED → FINDINGS_RECORDED → DRAFT_REPORT_SUBMITTED →
 * {FINALIZED | ESCALATED_TO_COMPREHENSIVE | SUSPENDED_FRAUD_INVESTIGATION}
 */
public class DeskAudit extends AggregateRoot {
    private final UUID id;
    private final UUID auditCaseId;
    private final String tin;
    private DeskAuditStatus status;
    private final List<EvidenceItem> evidenceItems;
    private SamplingMethod samplingMethod;
    private SampleSelection sampleSelection;
    private final List<DeskAuditFinding> findings;
    private DraftAuditReport draftReport;
    private String teamLeaderDecision;
    private String teamLeaderActorId;
    private String teamLeaderNarrative;
    private LocalDateTime teamLeaderDecidedAt;
    private String escalationDecision;
    private LocalDateTime escalatedAt;
    private FraudFlag fraudFlag;
    private LocalDateTime fraudFlaggedAt;
    private String fraudNotes;
    private final List<DocumentRequest> documentRequests;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

    private DeskAudit(UUID id, UUID auditCaseId, String tin, DeskAuditStatus status,
                     List<EvidenceItem> evidenceItems, SamplingMethod samplingMethod,
                     SampleSelection sampleSelection, List<DeskAuditFinding> findings,
                     DraftAuditReport draftReport, String teamLeaderDecision,
                     String teamLeaderActorId, String teamLeaderNarrative,
                     LocalDateTime teamLeaderDecidedAt, String escalationDecision,
                     LocalDateTime escalatedAt, FraudFlag fraudFlag,
                     LocalDateTime fraudFlaggedAt, String fraudNotes,
                     List<DocumentRequest> documentRequests, LocalDateTime createdAt,
                     LocalDateTime updatedAt, Long version) {
        this.id = id;
        this.auditCaseId = auditCaseId;
        this.tin = tin;
        this.status = status;
        this.evidenceItems = evidenceItems != null ? evidenceItems : new ArrayList<>();
        this.samplingMethod = samplingMethod;
        this.sampleSelection = sampleSelection;
        this.findings = findings != null ? findings : new ArrayList<>();
        this.draftReport = draftReport;
        this.teamLeaderDecision = teamLeaderDecision;
        this.teamLeaderActorId = teamLeaderActorId;
        this.teamLeaderNarrative = teamLeaderNarrative;
        this.teamLeaderDecidedAt = teamLeaderDecidedAt;
        this.escalationDecision = escalationDecision;
        this.escalatedAt = escalatedAt;
        this.fraudFlag = fraudFlag;
        this.fraudFlaggedAt = fraudFlaggedAt;
        this.fraudNotes = fraudNotes;
        this.documentRequests = documentRequests != null ? documentRequests : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    /**
     * Factory method to create a new desk audit in STARTED status.
     */
    public static DeskAudit start(UUID auditCaseId, String tin) {
        validateTin(tin);
        
        UUID deskAuditId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        DeskAudit deskAudit = new DeskAudit(
            deskAuditId,
            auditCaseId,
            tin,
            DeskAuditStatus.STARTED,
            new ArrayList<>(),
            null,
            null,
            new ArrayList<>(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new ArrayList<>(),
            now,
            now,
            0L
        );
        
        deskAudit.registerEvent(DeskAuditStartedEvent.of(deskAuditId, auditCaseId, tin));
        return deskAudit;
    }

    /**
     * Rehydrate a desk audit from persistence (used only by the persistence adapter).
     */
    public static DeskAudit rehydrate(UUID id, UUID auditCaseId, String tin, DeskAuditStatus status,
                                     List<EvidenceItem> evidenceItems, SamplingMethod samplingMethod,
                                     SampleSelection sampleSelection, List<DeskAuditFinding> findings,
                                     DraftAuditReport draftReport, String teamLeaderDecision,
                                     String teamLeaderActorId, String teamLeaderNarrative,
                                     LocalDateTime teamLeaderDecidedAt, String escalationDecision,
                                     LocalDateTime escalatedAt, FraudFlag fraudFlag,
                                     LocalDateTime fraudFlaggedAt, String fraudNotes,
                                     List<DocumentRequest> documentRequests, LocalDateTime createdAt,
                                     LocalDateTime updatedAt, Long version) {
        DeskAudit deskAudit = new DeskAudit(id, auditCaseId, tin, status, evidenceItems, samplingMethod,
                                           sampleSelection, findings, draftReport, teamLeaderDecision,
                                           teamLeaderActorId, teamLeaderNarrative, teamLeaderDecidedAt,
                                           escalationDecision, escalatedAt, fraudFlag, fraudFlaggedAt,
                                           fraudNotes, documentRequests, createdAt, updatedAt, version);
        // Discard any events that might have been registered during rehydration
        deskAudit.pullEvents();
        return deskAudit;
    }

    // Business methods

    public void recordEvidence(EvidenceItem evidence) {
        validateStatusTransition(DeskAuditStatus.STARTED, DeskAuditStatus.EVIDENCE_GATHERING);
        if (this.status != DeskAuditStatus.STARTED && this.status != DeskAuditStatus.EVIDENCE_GATHERING) {
            throw new DomainException("Cannot record evidence in status: " + this.status);
        }
        
        this.evidenceItems.add(evidence);
        this.status = DeskAuditStatus.EVIDENCE_GATHERING;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(EvidenceGatheredEvent.of(this.id, evidence.getEvidenceId(), evidence.getSourceType().name()));
    }

    public void requestDocuments(List<String> requestedDocumentTypes, String requestedByActorId) {
        validateStatusTransition(this.status, DeskAuditStatus.DOCUMENTS_REQUESTED);
        if (this.status != DeskAuditStatus.STARTED && this.status != DeskAuditStatus.EVIDENCE_GATHERING) {
            throw new DomainException("Cannot request documents in status: " + this.status);
        }
        
        String requestId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(7); // Default 7 days
        
        DocumentRequest request = new DocumentRequest(requestId, "MIXED", "Multiple documents requested", 
                                                     now, requestedByActorId, dueDate, 0);
        this.documentRequests.add(request);
        this.status = DeskAuditStatus.DOCUMENTS_REQUESTED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(DocumentsRequestedFromTaxpayerEvent.of(this.id, this.tin, requestedDocumentTypes));
    }

    public void receiveTaxpayerUpload(EvidenceItem document) {
        if (document.getSourceType() != EvidenceSourceType.TAXPAYER_UPLOAD) {
            throw new DomainException("Document upload must have source type TAXPAYER_UPLOAD");
        }
        recordEvidence(document);
    }

    public void determineSampling(SamplingMethod method, SampleSelection selection) {
        validateStatusTransition(this.status, DeskAuditStatus.SAMPLING_DETERMINED);
        if (this.status != DeskAuditStatus.EVIDENCE_GATHERING && this.status != DeskAuditStatus.DOCUMENTS_REQUESTED) {
            throw new DomainException("Cannot determine sampling in status: " + this.status);
        }
        
        this.samplingMethod = method;
        this.sampleSelection = selection;
        this.status = DeskAuditStatus.SAMPLING_DETERMINED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(SamplingMethodSelectedEvent.of(this.id, method.name(), selection.getSampleSize()));
    }

    public void recordFindings(List<DeskAuditFinding> newFindings) {
        validateStatusTransition(this.status, DeskAuditStatus.FINDINGS_RECORDED);
        if (this.status != DeskAuditStatus.SAMPLING_DETERMINED) {
            throw new DomainException("Cannot record findings in status: " + this.status);
        }
        if (newFindings == null || newFindings.isEmpty()) {
            throw new DomainException("Findings list cannot be null or empty");
        }
        
        this.findings.addAll(newFindings);
        this.status = DeskAuditStatus.FINDINGS_RECORDED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(DeskAuditFindingsRecordedEvent.of(this.id, this.findings.size()));
    }

    public void submitDraftReport(DraftAuditReport report, String preparedByActorId) {
        validateStatusTransition(this.status, DeskAuditStatus.DRAFT_REPORT_SUBMITTED);
        if (this.status != DeskAuditStatus.FINDINGS_RECORDED) {
            throw new DomainException("Cannot submit draft report in status: " + this.status);
        }
        
        this.draftReport = report;
        this.status = DeskAuditStatus.DRAFT_REPORT_SUBMITTED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(DraftAuditReportSubmittedEvent.of(this.id, preparedByActorId));
    }

    public void decideByTeamLeader(String decision, String actorId, String narrative) {
        if (this.status != DeskAuditStatus.DRAFT_REPORT_SUBMITTED) {
            throw new DomainException("Team leader can only decide on draft report in DRAFT_REPORT_SUBMITTED status");
        }
        if (decision == null || decision.isBlank()) {
            throw new DomainException("Decision cannot be null or blank");
        }
        
        this.teamLeaderDecision = decision;
        this.teamLeaderActorId = actorId;
        this.teamLeaderNarrative = narrative;
        this.teamLeaderDecidedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(TeamLeaderDeskAuditDecidedEvent.of(this.id, decision, actorId, narrative));
        
        // If escalation recommended, also register risk profile update event
        if ("ESCALATE_COMPREHENSIVE".equals(decision)) {
            registerEvent(RiskProfileUpdateRequestedEvent.of(this.id, this.tin, "Escalation to comprehensive audit recommended"));
        }
    }

    public void escalateToComprehensive(String escalatedByActorId) {
        if (!"ESCALATE_COMPREHENSIVE".equals(this.teamLeaderDecision)) {
            throw new DomainException("Can only escalate when team leader has recommended escalation");
        }
        
        this.escalationDecision = "APPROVED";
        this.escalatedAt = LocalDateTime.now();
        this.status = DeskAuditStatus.ESCALATED_TO_COMPREHENSIVE;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(DeskAuditEscalatedToComprehensiveEvent.of(this.id, this.auditCaseId, escalatedByActorId));
    }

    public void flagFraud(String indicatorNotes, String flaggedByActorId) {
        if (this.status == DeskAuditStatus.SUSPENDED_FRAUD_INVESTIGATION) {
            throw new DomainException("Desk audit is already flagged for fraud investigation");
        }
        if (this.status == DeskAuditStatus.FINALIZED || this.status == DeskAuditStatus.ESCALATED_TO_COMPREHENSIVE) {
            throw new DomainException("Cannot flag fraud on finalized or escalated desk audit");
        }
        
        String flagId = UUID.randomUUID().toString();
        this.fraudFlag = new FraudFlag(flagId, indicatorNotes, LocalDateTime.now(), flaggedByActorId);
        this.fraudFlaggedAt = LocalDateTime.now();
        this.fraudNotes = indicatorNotes;
        this.status = DeskAuditStatus.SUSPENDED_FRAUD_INVESTIGATION;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(FraudIndicatorFlaggedEvent.of(this.id, this.tin, indicatorNotes, flaggedByActorId));
    }

    public void resumeAfterFraudCleared() {
        if (this.status != DeskAuditStatus.SUSPENDED_FRAUD_INVESTIGATION) {
            throw new DomainException("Can only resume from SUSPENDED_FRAUD_INVESTIGATION status");
        }
        if (this.fraudFlag == null) {
            throw new DomainException("Cannot resume: no fraud flag exists");
        }
        
        this.status = DeskAuditStatus.FINDINGS_RECORDED;
        this.updatedAt = LocalDateTime.now();
        // Note: fraud flag remains for audit trail
    }

    public void finalize(String finalizedByActorId) {
        if (this.status != DeskAuditStatus.DRAFT_REPORT_SUBMITTED) {
            throw new DomainException("Can only finalize from DRAFT_REPORT_SUBMITTED status");
        }
        if (!"APPROVE_FINALIZE".equals(this.teamLeaderDecision)) {
            throw new DomainException("Can only finalize when team leader decision is APPROVE_FINALIZE");
        }
        
        this.status = DeskAuditStatus.FINALIZED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(DeskAuditFinalizedEvent.of(this.id, finalizedByActorId));
    }

    // Helper methods

    public boolean hasOverdueDocumentRequests() {
        if (documentRequests == null || documentRequests.isEmpty()) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return documentRequests.stream()
            .anyMatch(request -> request.getDueDate() != null && request.getDueDate().isBefore(now));
    }

    private void validateStatusTransition(DeskAuditStatus from, DeskAuditStatus to) {
        // Status transition validation logic
    }

    private static void validateTin(String tin) {
        if (tin == null || tin.isBlank()) {
            throw new DomainException("TIN cannot be null or blank");
        }
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public UUID getAuditCaseId() {
        return auditCaseId;
    }

    public String getTin() {
        return tin;
    }

    public DeskAuditStatus getStatus() {
        return status;
    }

    public List<EvidenceItem> getEvidenceItems() {
        return List.copyOf(evidenceItems);
    }

    public SamplingMethod getSamplingMethod() {
        return samplingMethod;
    }

    public SampleSelection getSampleSelection() {
        return sampleSelection;
    }

    public List<DeskAuditFinding> getFindings() {
        return List.copyOf(findings);
    }

    public DraftAuditReport getDraftReport() {
        return draftReport;
    }

    public String getTeamLeaderDecision() {
        return teamLeaderDecision;
    }

    public String getTeamLeaderActorId() {
        return teamLeaderActorId;
    }

    public String getTeamLeaderNarrative() {
        return teamLeaderNarrative;
    }

    public LocalDateTime getTeamLeaderDecidedAt() {
        return teamLeaderDecidedAt;
    }

    public String getEscalationDecision() {
        return escalationDecision;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public FraudFlag getFraudFlag() {
        return fraudFlag;
    }

    public LocalDateTime getFraudFlaggedAt() {
        return fraudFlaggedAt;
    }

    public String getFraudNotes() {
        return fraudNotes;
    }

    public List<DocumentRequest> getDocumentRequests() {
        return List.copyOf(documentRequests);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    void setVersion(Long version) {
        this.version = version;
    }
}
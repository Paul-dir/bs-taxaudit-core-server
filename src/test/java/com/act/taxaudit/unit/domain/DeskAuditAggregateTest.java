package com.act.taxaudit.unit.domain;

import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.DomainException;
import com.act.taxaudit.domain.model.EvidenceItem;
import com.act.taxaudit.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeskAuditAggregateTest {

    @Test
    void shouldStartDeskAudit() {
        UUID auditCaseId = UUID.randomUUID();
        String tin = "123456789";

        DeskAudit deskAudit = DeskAudit.start(auditCaseId, tin);

        assertNotNull(deskAudit.getId());
        assertEquals(auditCaseId, deskAudit.getAuditCaseId());
        assertEquals(tin, deskAudit.getTin());
        assertEquals(DeskAuditStatus.STARTED, deskAudit.getStatus());
        assertTrue(deskAudit.getEvidenceItems().isEmpty());
        assertTrue(deskAudit.getFindings().isEmpty());
        assertTrue(deskAudit.getDocumentRequests().isEmpty());
        assertNotNull(deskAudit.getCreatedAt());
        assertNotNull(deskAudit.getUpdatedAt());
        assertEquals(0L, deskAudit.getVersion());
        assertEquals(1, deskAudit.getUncommittedEvents().size());
    }

    @Test
    void shouldRecordEvidence() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        EvidenceItem evidence = new EvidenceItem(
            "ev-1",
            EvidenceSourceType.INTERNAL_SYSTEM,
            "Test evidence",
            null,
            LocalDateTime.now(),
            "actor-1"
        );

        deskAudit.recordEvidence(evidence);

        assertEquals(DeskAuditStatus.EVIDENCE_GATHERING, deskAudit.getStatus());
        assertEquals(1, deskAudit.getEvidenceItems().size());
        assertEquals(2, deskAudit.getUncommittedEvents().size()); // Started + EvidenceGathered
    }

    @Test
    void shouldRequestDocuments() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");

        deskAudit.requestDocuments(List.of("INVOICE", "RECEIPT"), "actor-1");

        assertEquals(DeskAuditStatus.DOCUMENTS_REQUESTED, deskAudit.getStatus());
        assertEquals(1, deskAudit.getDocumentRequests().size());
        assertEquals(2, deskAudit.getUncommittedEvents().size());
    }

    @Test
    void shouldReceiveTaxpayerUpload() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        EvidenceItem upload = new EvidenceItem(
            "upload-1",
            EvidenceSourceType.TAXPAYER_UPLOAD,
            "Taxpayer document",
            "dms-ref-1",
            LocalDateTime.now(),
            "taxpayer-1"
        );

        deskAudit.receiveTaxpayerUpload(upload);

        assertEquals(DeskAuditStatus.EVIDENCE_GATHERING, deskAudit.getStatus());
        assertEquals(1, deskAudit.getEvidenceItems().size());
        assertEquals(EvidenceSourceType.TAXPAYER_UPLOAD, deskAudit.getEvidenceItems().get(0).getSourceType());
    }

    @Test
    void shouldThrowWhenReceivingNonTaxpayerUpload() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        EvidenceItem evidence = new EvidenceItem(
            "ev-1",
            EvidenceSourceType.INTERNAL_SYSTEM,
            "Test",
            null,
            LocalDateTime.now(),
            "actor-1"
        );

        assertThrows(DomainException.class, () -> deskAudit.receiveTaxpayerUpload(evidence));
    }

    @Test
    void shouldDetermineSampling() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        deskAudit.recordEvidence(new EvidenceItem("ev-1", EvidenceSourceType.INTERNAL_SYSTEM, "Test", null, LocalDateTime.now(), "actor-1"));

        SampleSelection selection = new SampleSelection(
            SamplingMethod.STRATIFIED,
            "High value transactions",
            List.of("item-1", "item-2", "item-3"),
            3
        );

        deskAudit.determineSampling(SamplingMethod.STRATIFIED, selection);

        assertEquals(DeskAuditStatus.SAMPLING_DETERMINED, deskAudit.getStatus());
        assertEquals(SamplingMethod.STRATIFIED, deskAudit.getSamplingMethod());
        assertNotNull(deskAudit.getSampleSelection());
    }

    @Test
    void shouldRecordFindings() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        deskAudit.recordEvidence(new EvidenceItem("ev-1", EvidenceSourceType.INTERNAL_SYSTEM, "Test", null, LocalDateTime.now(), "actor-1"));
        deskAudit.determineSampling(SamplingMethod.RANDOM, new SampleSelection(SamplingMethod.RANDOM, "Test", List.of("1"), 1));

        List<DeskAuditFinding> findings = List.of(
            new DeskAuditFinding("VAT", Severity.MAJOR, "Missing VAT registration", true),
            new DeskAuditFinding("INCOME", Severity.CRITICAL, "Underreported income", true)
        );

        deskAudit.recordFindings(findings);

        assertEquals(DeskAuditStatus.FINDINGS_RECORDED, deskAudit.getStatus());
        assertEquals(2, deskAudit.getFindings().size());
    }

    @Test
    void shouldSubmitDraftReport() {
        DeskAudit deskAudit = createDeskAuditWithFindings();

        DraftAuditReport report = new DraftAuditReport(
            "Audit narrative",
            "Major findings in VAT",
            LocalDateTime.now(),
            "auditor-1"
        );

        deskAudit.submitDraftReport(report, "auditor-1");

        assertEquals(DeskAuditStatus.DRAFT_REPORT_SUBMITTED, deskAudit.getStatus());
        assertNotNull(deskAudit.getDraftReport());
    }

    @Test
    void shouldFinalizeDeskAudit() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.submitDraftReport(new DraftAuditReport("Narrative", "Summary", LocalDateTime.now(), "auditor-1"), "auditor-1");
        deskAudit.decideByTeamLeader("APPROVE_FINALIZE", "team-leader-1", "Approved");

        deskAudit.finalize("team-leader-1");

        assertEquals(DeskAuditStatus.FINALIZED, deskAudit.getStatus());
    }

    @Test
    void shouldEscalateToComprehensive() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.submitDraftReport(new DraftAuditReport("Narrative", "Summary", LocalDateTime.now(), "auditor-1"), "auditor-1");
        deskAudit.decideByTeamLeader("ESCALATE_COMPREHENSIVE", "team-leader-1", "Major issues found");
        deskAudit.escalateToComprehensive("director-1");

        assertEquals(DeskAuditStatus.ESCALATED_TO_COMPREHENSIVE, deskAudit.getStatus());
        assertEquals("APPROVED", deskAudit.getEscalationDecision());
    }

    @Test
    void shouldFlagFraud() {
        DeskAudit deskAudit = createDeskAuditWithFindings();

        deskAudit.flagFraud("Suspicious transaction pattern", "auditor-1");

        assertEquals(DeskAuditStatus.SUSPENDED_FRAUD_INVESTIGATION, deskAudit.getStatus());
        assertNotNull(deskAudit.getFraudFlag());
    }

    @Test
    void shouldResumeAfterFraudCleared() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.flagFraud("Suspicious pattern", "auditor-1");

        deskAudit.resumeAfterFraudCleared();

        assertEquals(DeskAuditStatus.FINDINGS_RECORDED, deskAudit.getStatus());
    }

    @Test
    void shouldThrowWhenFinalizingWithoutApproval() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.submitDraftReport(new DraftAuditReport("Narrative", "Summary", LocalDateTime.now(), "auditor-1"), "auditor-1");
        deskAudit.decideByTeamLeader("REJECT_REWORK", "team-leader-1", "Needs revision");

        assertThrows(DomainException.class, () -> deskAudit.finalize("team-leader-1"));
    }

    @Test
    void shouldThrowWhenEscalatingWithoutRecommendation() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.submitDraftReport(new DraftAuditReport("Narrative", "Summary", LocalDateTime.now(), "auditor-1"), "auditor-1");
        deskAudit.decideByTeamLeader("APPROVE_FINALIZE", "team-leader-1", "Approved");

        assertThrows(DomainException.class, () -> deskAudit.escalateToComprehensive("director-1"));
    }

    @Test
    void shouldNotAllowDuplicateFraudFlag() {
        DeskAudit deskAudit = createDeskAuditWithFindings();
        deskAudit.flagFraud("Pattern 1", "auditor-1");

        assertThrows(DomainException.class, () -> deskAudit.flagFraud("Pattern 2", "auditor-1"));
    }

    private DeskAudit createDeskAuditWithFindings() {
        DeskAudit deskAudit = DeskAudit.start(UUID.randomUUID(), "123456789");
        deskAudit.recordEvidence(new EvidenceItem("ev-1", EvidenceSourceType.INTERNAL_SYSTEM, "Test", null, LocalDateTime.now(), "actor-1"));
        deskAudit.determineSampling(SamplingMethod.RANDOM, new SampleSelection(SamplingMethod.RANDOM, "Test", List.of("1"), 1));
        deskAudit.recordFindings(List.of(new DeskAuditFinding("TEST", Severity.MINOR, "Test finding", false)));
        return deskAudit;
    }
}
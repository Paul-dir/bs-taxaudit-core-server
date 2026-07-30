package com.act.taxaudit.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "desk_audits")
public class DeskAuditEntity {

    @Id
    private UUID id;

    @Column(name = "audit_case_id", nullable = false)
    private UUID auditCaseId;

    @Column(nullable = false, length = 32)
    private String tin;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "evidence_items_json", columnDefinition = "jsonb")
    private String evidenceItemsJson;

    @Column(name = "document_requests_json", columnDefinition = "jsonb")
    private String documentRequestsJson;

    @Column(name = "sampling_method", length = 16)
    private String samplingMethod;

    @Column(name = "sample_selection_json", columnDefinition = "jsonb")
    private String sampleSelectionJson;

    @Column(name = "findings_json", columnDefinition = "jsonb")
    private String findingsJson;

    @Column(name = "draft_report_json", columnDefinition = "jsonb")
    private String draftReportJson;

    @Column(name = "team_leader_decision", length = 32)
    private String teamLeaderDecision;

    @Column(name = "team_leader_actor_id", length = 128)
    private String teamLeaderActorId;

    @Column(name = "team_leader_narrative", length = 2048)
    private String teamLeaderNarrative;

    @Column(name = "team_leader_decided_at")
    private OffsetDateTime teamLeaderDecidedAt;

    @Column(name = "escalation_decision", length = 32)
    private String escalationDecision;

    @Column(name = "escalated_at")
    private OffsetDateTime escalatedAt;

    @Column(name = "fraud_flag_json", columnDefinition = "jsonb")
    private String fraudFlagJson;

    @Column(name = "fraud_notes", length = 2048)
    private String fraudNotes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;

    public DeskAuditEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAuditCaseId() { return auditCaseId; }
    public void setAuditCaseId(UUID auditCaseId) { this.auditCaseId = auditCaseId; }
    public String getTin() { return tin; }
    public void setTin(String tin) { this.tin = tin; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEvidenceItemsJson() { return evidenceItemsJson; }
    public void setEvidenceItemsJson(String evidenceItemsJson) { this.evidenceItemsJson = evidenceItemsJson; }
    public String getDocumentRequestsJson() { return documentRequestsJson; }
    public void setDocumentRequestsJson(String documentRequestsJson) { this.documentRequestsJson = documentRequestsJson; }
    public String getSamplingMethod() { return samplingMethod; }
    public void setSamplingMethod(String samplingMethod) { this.samplingMethod = samplingMethod; }
    public String getSampleSelectionJson() { return sampleSelectionJson; }
    public void setSampleSelectionJson(String sampleSelectionJson) { this.sampleSelectionJson = sampleSelectionJson; }
    public String getFindingsJson() { return findingsJson; }
    public void setFindingsJson(String findingsJson) { this.findingsJson = findingsJson; }
    public String getDraftReportJson() { return draftReportJson; }
    public void setDraftReportJson(String draftReportJson) { this.draftReportJson = draftReportJson; }
    public String getTeamLeaderDecision() { return teamLeaderDecision; }
    public void setTeamLeaderDecision(String teamLeaderDecision) { this.teamLeaderDecision = teamLeaderDecision; }
    public String getTeamLeaderActorId() { return teamLeaderActorId; }
    public void setTeamLeaderActorId(String teamLeaderActorId) { this.teamLeaderActorId = teamLeaderActorId; }
    public String getTeamLeaderNarrative() { return teamLeaderNarrative; }
    public void setTeamLeaderNarrative(String teamLeaderNarrative) { this.teamLeaderNarrative = teamLeaderNarrative; }
    public OffsetDateTime getTeamLeaderDecidedAt() { return teamLeaderDecidedAt; }
    public void setTeamLeaderDecidedAt(OffsetDateTime teamLeaderDecidedAt) { this.teamLeaderDecidedAt = teamLeaderDecidedAt; }
    public String getEscalationDecision() { return escalationDecision; }
    public void setEscalationDecision(String escalationDecision) { this.escalationDecision = escalationDecision; }
    public OffsetDateTime getEscalatedAt() { return escalatedAt; }
    public void setEscalatedAt(OffsetDateTime escalatedAt) { this.escalatedAt = escalatedAt; }
    public String getFraudFlagJson() { return fraudFlagJson; }
    public void setFraudFlagJson(String fraudFlagJson) { this.fraudFlagJson = fraudFlagJson; }
    public String getFraudNotes() { return fraudNotes; }
    public void setFraudNotes(String fraudNotes) { this.fraudNotes = fraudNotes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
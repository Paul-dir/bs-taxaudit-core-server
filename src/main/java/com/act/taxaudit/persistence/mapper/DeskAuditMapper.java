package com.act.taxaudit.persistence.mapper;

import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.event.*;
import com.act.taxaudit.domain.exception.DomainException;
import com.act.taxaudit.domain.model.EvidenceItem;
import com.act.taxaudit.domain.valueobject.*;
import com.act.taxaudit.persistence.jpa.entity.DeskAuditEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeskAuditMapper {

    private final ObjectMapper objectMapper;

    public DeskAuditMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public DeskAuditEntity toEntity(DeskAudit deskAudit) {
        DeskAuditEntity entity = new DeskAuditEntity();
        entity.setId(deskAudit.getId());
        entity.setAuditCaseId(deskAudit.getAuditCaseId());
        entity.setTin(deskAudit.getTin());
        entity.setStatus(deskAudit.getStatus().name());
        entity.setEvidenceItemsJson(toJson(deskAudit.getEvidenceItems()));
        entity.setDocumentRequestsJson(toJson(deskAudit.getDocumentRequests()));
        entity.setSamplingMethod(deskAudit.getSamplingMethod() != null ? deskAudit.getSamplingMethod().name() : null);
        entity.setSampleSelectionJson(toJson(deskAudit.getSampleSelection()));
        entity.setFindingsJson(toJson(deskAudit.getFindings()));
        entity.setDraftReportJson(toJson(deskAudit.getDraftReport()));
        entity.setTeamLeaderDecision(deskAudit.getTeamLeaderDecision());
        entity.setTeamLeaderActorId(deskAudit.getTeamLeaderActorId());
        entity.setTeamLeaderNarrative(deskAudit.getTeamLeaderNarrative());
        entity.setTeamLeaderDecidedAt(toOffsetDateTime(deskAudit.getTeamLeaderDecidedAt()));
        entity.setEscalationDecision(deskAudit.getEscalationDecision());
        entity.setEscalatedAt(toOffsetDateTime(deskAudit.getEscalatedAt()));
        entity.setFraudFlagJson(toJson(deskAudit.getFraudFlag()));
        entity.setFraudNotes(deskAudit.getFraudNotes());
        entity.setCreatedAt(toOffsetDateTime(deskAudit.getCreatedAt()));
        entity.setUpdatedAt(toOffsetDateTime(deskAudit.getUpdatedAt()));
        entity.setVersion(deskAudit.getVersion());
        return entity;
    }

    public DeskAudit toDomain(DeskAuditEntity entity) {
        List<EvidenceItem> evidenceItems = fromJsonList(entity.getEvidenceItemsJson(), EvidenceItem.class);
        List<DocumentRequest> documentRequests = fromJsonList(entity.getDocumentRequestsJson(), DocumentRequest.class);
        List<DeskAuditFinding> findings = fromJsonList(entity.getFindingsJson(), DeskAuditFinding.class);
        SampleSelection sampleSelection = fromJson(entity.getSampleSelectionJson(), SampleSelection.class);
        DraftAuditReport draftReport = fromJson(entity.getDraftReportJson(), DraftAuditReport.class);
        FraudFlag fraudFlag = fromJson(entity.getFraudFlagJson(), FraudFlag.class);
        SamplingMethod samplingMethod = entity.getSamplingMethod() != null ? SamplingMethod.valueOf(entity.getSamplingMethod()) : null;
        DeskAuditStatus status = DeskAuditStatus.valueOf(entity.getStatus());

        return DeskAudit.rehydrate(
            entity.getId(),
            entity.getAuditCaseId(),
            entity.getTin(),
            status,
            evidenceItems,
            samplingMethod,
            sampleSelection,
            findings,
            draftReport,
            entity.getTeamLeaderDecision(),
            entity.getTeamLeaderActorId(),
            entity.getTeamLeaderNarrative(),
            toLocalDateTime(entity.getTeamLeaderDecidedAt()),
            entity.getEscalationDecision(),
            toLocalDateTime(entity.getEscalatedAt()),
            fraudFlag,
            toLocalDateTime(null),
            entity.getFraudNotes(),
            documentRequests,
            toLocalDateTime(entity.getCreatedAt()),
            toLocalDateTime(entity.getUpdatedAt()),
            entity.getVersion()
        );
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new DomainException("Failed to serialize object: " + e.getMessage());
        }
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new DomainException("Failed to deserialize object: " + e.getMessage());
        }
    }

    private <T> List<T> fromJsonList(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new DomainException("Failed to deserialize list: " + e.getMessage());
        }
    }

    private OffsetDateTime toOffsetDateTime(LocalDateTime ldt) {
        return ldt != null ? ldt.atOffset(ZoneOffset.UTC) : null;
    }

    private LocalDateTime toLocalDateTime(OffsetDateTime odt) {
        return odt != null ? odt.toLocalDateTime() : null;
    }
}
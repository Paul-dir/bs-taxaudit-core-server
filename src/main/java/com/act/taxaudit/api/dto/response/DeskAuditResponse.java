package com.act.taxaudit.api.dto.response;

import com.act.taxaudit.domain.aggregate.DeskAudit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record DeskAuditResponse(
    UUID id,
    UUID auditCaseId,
    String tin,
    String status,
    List<String> evidenceIds,
    String samplingMethod,
    int findingCount,
    String teamLeaderDecision,
    String escalationDecision,
    boolean hasFraudFlag,
    boolean hasDraftReport,
    int documentRequestCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static DeskAuditResponse from(DeskAudit da) {
        return new DeskAuditResponse(
            da.getId(),
            da.getAuditCaseId(),
            da.getTin(),
            da.getStatus().name(),
            da.getEvidenceItems().stream().map(e -> e.getEvidenceId()).collect(Collectors.toList()),
            da.getSamplingMethod() != null ? da.getSamplingMethod().name() : null,
            da.getFindings().size(),
            da.getTeamLeaderDecision(),
            da.getEscalationDecision(),
            da.getFraudFlag() != null,
            da.getDraftReport() != null,
            da.getDocumentRequests().size(),
            da.getCreatedAt(),
            da.getUpdatedAt()
        );
    }
}
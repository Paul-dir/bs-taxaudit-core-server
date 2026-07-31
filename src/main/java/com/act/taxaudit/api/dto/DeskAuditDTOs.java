package com.act.taxaudit.api.dto;

import com.act.taxaudit.domain.valueobject.DeskAuditFinding;
import com.act.taxaudit.domain.valueobject.SamplingMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Strict data contracts for API boundaries.
 * These DTOs prevent domain internals from leaking across module boundaries.
 * MANDATORY for inter-module communication in ITAS ecosystem.
 */
public final class DeskAuditDTOs {

    private DeskAuditDTOs() {}

    public record StartDeskAuditRequest(UUID auditCaseId, String tin) {}

    public record GatherEvidenceRequest(String actorId) {}

    public record RequestDocumentsRequest(List<String> documentTypes, String requestedByActorId) {}

    public record DetermineSamplingRequest(SamplingMethod method, String criteria,
                                           List<String> selectedItems, int sampleSize) {}

    public record RecordFindingsRequest(List<DeskAuditFinding> findings) {}

    public record DeskAuditResponse(
            UUID id,
            UUID auditCaseId,
            String tin,
            String status,
            List<String> evidenceIds,
            String samplingMethod,
            int findingCount,
            String teamLeaderDecision,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record ModuleCompletionNotification(
            String module,
            String eventType,
            UUID aggregateId,
            String status,
            LocalDateTime completedAt
    ) {}
}
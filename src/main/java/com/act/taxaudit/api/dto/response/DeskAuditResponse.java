package com.act.taxaudit.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
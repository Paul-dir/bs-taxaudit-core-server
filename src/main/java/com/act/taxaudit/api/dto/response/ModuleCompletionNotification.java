package com.act.taxaudit.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModuleCompletionNotification(
    String module,
    String eventType,
    UUID aggregateId,
    String status,
    LocalDateTime completedAt
) {}
package com.mor.itas.planning.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
public class AssignmentLog {
    private final UUID id;
    private final UUID auditCaseId;
    private final UUID fromUserId;
    private final UUID toUserId;
    private final Instant transitionDate;
    private final String reason;
    private final String statusTransition;
}

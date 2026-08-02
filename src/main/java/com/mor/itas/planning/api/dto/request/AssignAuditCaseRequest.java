package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class AssignAuditCaseRequest {
    private UUID teamLeaderId;
    private UUID requestedAuditorId;
    private List<String> requiredSkills;
}

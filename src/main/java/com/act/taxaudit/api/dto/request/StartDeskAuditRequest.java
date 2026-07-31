package com.act.taxaudit.api.dto.request;

import java.util.UUID;

public record StartDeskAuditRequest(UUID auditCaseId, String tin) {}
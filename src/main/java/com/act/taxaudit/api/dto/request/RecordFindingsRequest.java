package com.act.taxaudit.api.dto.request;

import java.util.List;

public record RecordFindingsRequest(java.util.List<com.act.taxaudit.domain.valueobject.DeskAuditFinding> findings) {}

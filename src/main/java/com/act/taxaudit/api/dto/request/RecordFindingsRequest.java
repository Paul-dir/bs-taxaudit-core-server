package com.act.taxaudit.api.dto.request;

import com.act.taxaudit.domain.valueobject.DeskAuditFinding;
import java.util.List;

public record RecordFindingsRequest(List<DeskAuditFinding> findings) {}
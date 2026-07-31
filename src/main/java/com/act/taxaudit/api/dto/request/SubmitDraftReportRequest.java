package com.act.taxaudit.api.dto.request;

import com.act.taxaudit.domain.valueobject.DraftAuditReport;
import java.time.LocalDateTime;

public record SubmitDraftReportRequest(String narrative, String findingsSummary, String preparedByActorId) {
    public DraftAuditReport toReport() {
        return new DraftAuditReport(narrative, findingsSummary, LocalDateTime.now(), preparedByActorId);
    }
}
package com.mor.itas.planning.api.dto.response;

import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.Data;
import java.util.List;

@Data
public class AuditCaseListResponse {
    private List<AuditCase> auditCases;

    public AuditCaseListResponse(List<AuditCase> auditCases) {
        this.auditCases = auditCases;
    }
}

package com.mor.itas.planning.api.dto.response;

import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.Data;

@Data
public class AuditCaseResponse {
    private AuditCase auditCase;

    public AuditCaseResponse(AuditCase auditCase) {
        this.auditCase = auditCase;
    }
}

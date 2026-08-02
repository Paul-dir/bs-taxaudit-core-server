package com.mor.itas.planning.api.dto.response;

import com.itas.bs.taxaudit.domain.model.AuditReferral;
import lombok.Data;

@Data
public class AuditReferralResponse {
    private AuditReferral auditReferral;

    public AuditReferralResponse(AuditReferral auditReferral) {
        this.auditReferral = auditReferral;
    }
}

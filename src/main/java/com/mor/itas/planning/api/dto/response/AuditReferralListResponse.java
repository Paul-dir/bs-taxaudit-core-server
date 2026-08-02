package com.mor.itas.planning.api.dto.response;

import com.itas.bs.taxaudit.domain.model.AuditReferral;
import lombok.Data;
import java.util.List;

@Data
public class AuditReferralListResponse {
    private List<AuditReferral> auditReferrals;

    public AuditReferralListResponse(List<AuditReferral> auditReferrals) {
        this.auditReferrals = auditReferrals;
    }
}

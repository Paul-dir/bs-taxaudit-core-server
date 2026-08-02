package com.mor.itas.planning.api.dto.request;

import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import lombok.Data;

@Data
public class IntakeAuditReferralRequest {
    private AuditCaseSource source;
    private String referringEntity;
    private String referenceDetails;
    private String relatedTaxpayerTin;
}

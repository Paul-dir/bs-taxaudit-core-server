package com.mor.itas.planning.domain.model;

import com.itas.bs.taxaudit.domain.aggregate.AggregateRoot;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.ReferralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
public class AuditReferral implements AggregateRoot {
    private final UUID id;
    private final AuditCaseSource sourceType;
    private final String referringEntity;
    private final String referenceDetails;
    private final String relatedTaxpayerTin;
    private ReferralStatus status;
    private UUID resolvedCaseId;
    private final Instant receivedAt;
    private UUID triagedBy;
    private String triageReason;

    public void triage(boolean accept, UUID triagedBy, String reason) {
        if (this.status != ReferralStatus.RECEIVED) {
            throw new IllegalStateException("Referral must be in RECEIVED state to triage.");
        }
        this.status = accept ? ReferralStatus.ACCEPTED : ReferralStatus.DECLINED;
        this.triagedBy = triagedBy;
        this.triageReason = reason;
    }

    public void resolve(UUID caseId) {
        if (this.status != ReferralStatus.ACCEPTED) {
            throw new IllegalStateException("Referral must be in ACCEPTED state to resolve.");
        }
        this.status = ReferralStatus.RESOLVED;
        this.resolvedCaseId = caseId;
    }
}

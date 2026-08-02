package com.mor.itas.planning.persistence.jpa.entity;

import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.ReferralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_referral")
@Getter
@Setter
public class AuditReferralEntity {
    @Id
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private AuditCaseSource sourceType;
    
    private String referringEntity;
    private String referenceDetails;
    private String relatedTaxpayerTin;
    
    @Enumerated(EnumType.STRING)
    private ReferralStatus status;
    
    private Instant receivedAt;
    private UUID resolvedCaseId;
}

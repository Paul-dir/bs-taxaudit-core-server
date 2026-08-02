package com.mor.itas.planning.persistence.jpa.entity;

import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseStatus;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "audit_case")
@Getter
@Setter
public class AuditCaseEntity {
    @Id
    private UUID id;
    
    private String caseReferenceNumber;
    
    @Enumerated(EnumType.STRING)
    private AuditCaseStatus status;
    
    private UUID taxpayerPartyId;
    private String taxpayerName;
    private String tin;
    private Double riskScore;
    private java.math.BigDecimal revenueAtRisk;
    private Integer estimatedHours;
    
    @Enumerated(EnumType.STRING)
    private AuditType auditType;
    
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
    
    @Enumerated(EnumType.STRING)
    private AuditCaseSource source;
    
    private UUID sourceReferralId;
    private UUID annualPlanId;
    private UUID assignedAuditorId;
    private UUID teamLeaderId;
}

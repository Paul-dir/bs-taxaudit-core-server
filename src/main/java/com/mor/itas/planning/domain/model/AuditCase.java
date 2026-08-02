package com.mor.itas.planning.domain.model;

import com.itas.bs.taxaudit.domain.aggregate.AggregateRoot;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseStatus;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import com.itas.bs.taxaudit.domain.valueobject.TreatmentPlan;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
public class AuditCase implements AggregateRoot {
    private final UUID id;
    private final String caseReferenceNumber;
    private AuditCaseStatus status;
    private final UUID taxpayerPartyId;
    private final String taxpayerName;
    private final String tin;
    private UUID assignedAuditorId;
    private UUID teamLeaderId;
    private final AuditType auditType;
    private final RiskLevel riskLevel;
    private final Double riskScore;
    private final java.math.BigDecimal revenueAtRisk;
    private final Integer estimatedHours;
    private final AuditCaseSource source;
    private final UUID sourceReferralId;
    private TreatmentPlan treatmentPlan;
    private final UUID annualPlanId;
    private LocalDate tentativeStartDate;
    private LocalDate tentativeEndDate;
    private boolean joinAuditFlag;

    public void selectForAudit(TreatmentPlan treatmentPlan) {
        if (this.status != AuditCaseStatus.CREATED) {
            throw new IllegalStateException("Case must be in CREATED state to select.");
        }
        this.status = AuditCaseStatus.SELECTED_FOR_AUDIT;
        this.treatmentPlan = treatmentPlan;
    }

    public void assignAuditor(UUID auditorId, UUID teamLeaderId) {
        if (this.status != AuditCaseStatus.SELECTED_FOR_AUDIT && this.status != AuditCaseStatus.CREATED) {
            throw new IllegalStateException("Case must be in SELECTED_FOR_AUDIT or CREATED state to assign.");
        }
        this.status = AuditCaseStatus.ASSIGNED;
        this.assignedAuditorId = auditorId;
        this.teamLeaderId = teamLeaderId;
    }

    public void reassignAuditor(UUID newAuditorId) {
        if (this.status != AuditCaseStatus.ASSIGNED) {
            throw new IllegalStateException("Case must be in ASSIGNED state to reassign.");
        }
        this.assignedAuditorId = newAuditorId;
    }

    public void markJointAudit() {
        this.joinAuditFlag = true;
        this.status = AuditCaseStatus.PENDING_JOINT_AUDIT_FORMATION;
    }

    public void prioritizeAndStore() {
        if (this.status != AuditCaseStatus.PENDING_PRIORITIZATION) {
            throw new IllegalStateException("Case must be in PENDING_PRIORITIZATION state to be stored.");
        }
        this.status = AuditCaseStatus.STORED_FOR_ASSIGNMENT;
    }

    public void assignToTeamLeader(UUID teamLeaderId) {
        if (this.status != AuditCaseStatus.STORED_FOR_ASSIGNMENT) {
            throw new IllegalStateException("Case must be in STORED_FOR_ASSIGNMENT state to assign to a Team Leader.");
        }
        this.status = AuditCaseStatus.ASSIGNED_TO_TEAM_LEADER;
        this.teamLeaderId = teamLeaderId;
    }

    public void assignToAuditor(UUID auditorId) {
        if (this.status != AuditCaseStatus.ASSIGNED_TO_TEAM_LEADER) {
            throw new IllegalStateException("Case must be in ASSIGNED_TO_TEAM_LEADER state to assign to an Auditor.");
        }
        this.status = AuditCaseStatus.ASSIGNED_TO_AUDITOR;
        this.assignedAuditorId = auditorId;
    }

    public void updateTimeline(LocalDate start, LocalDate end) {
        this.tentativeStartDate = start;
        this.tentativeEndDate = end;
    }
}

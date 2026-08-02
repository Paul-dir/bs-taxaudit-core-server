package com.mor.itas.planning.domain.model;

import com.itas.bs.taxaudit.domain.aggregate.AggregateRoot;
import com.itas.bs.taxaudit.domain.valueobject.AuditPlanStatus;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.PlanVersion;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
public class AnnualAuditPlan implements AggregateRoot {
    private final UUID id;
    private final int year;
    private AuditPlanStatus status;
    private final int totalCasesPlanned;
    private int totalCasesCreated;
    private final UUID directorId;
    private UUID seniorManagementId;
    private final List<PlanVersion> versions;
    private final Map<AuditType, Integer> plannedVolumeByType;

    public void submitToDirector(UUID actorId) {
        if (this.status != AuditPlanStatus.DRAFT && this.status != AuditPlanStatus.RETURNED_TO_DIRECTOR) {
            throw new IllegalStateException("Plan must be in DRAFT or RETURNED_TO_DIRECTOR state to submit.");
        }
        this.status = AuditPlanStatus.SUBMITTED_TO_DIRECTOR;
        addVersion("Plan submitted to Director for review", actorId);
    }

    public void approveByDirector(UUID actorId) {
        if (this.status != AuditPlanStatus.SUBMITTED_TO_DIRECTOR) {
            throw new IllegalStateException("Plan must be in SUBMITTED_TO_DIRECTOR state to approve.");
        }
        this.status = AuditPlanStatus.APPROVED_BY_DIRECTOR;
        addVersion("Plan approved by Director", actorId);
    }

    public void requestBusinessUnitFeedback(UUID actorId) {
        if (this.status != AuditPlanStatus.APPROVED_BY_DIRECTOR) {
            throw new IllegalStateException("Plan must be in APPROVED_BY_DIRECTOR state to initiate feedback loop.");
        }
        this.status = AuditPlanStatus.AWAITING_BUSINESS_FEEDBACK;
        addVersion("Plan shared for Business Unit Feedback", actorId);
    }

    public void finalizeByDirector(UUID actorId) {
        if (this.status != AuditPlanStatus.AWAITING_BUSINESS_FEEDBACK && this.status != AuditPlanStatus.APPROVED_BY_DIRECTOR) {
            throw new IllegalStateException("Plan must be approved or awaiting feedback to be finalized.");
        }
        this.status = AuditPlanStatus.SUBMITTED_TO_SENIOR_MGMT;
        addVersion("Plan finalized and submitted to Senior Management", actorId);
    }

    public void approveBySeniorManagement(UUID seniorMgmtId) {
        if (this.status != AuditPlanStatus.SUBMITTED_TO_SENIOR_MGMT) {
            throw new IllegalStateException("Plan must be in SUBMITTED_TO_SENIOR_MGMT state to approve.");
        }
        this.status = AuditPlanStatus.APPROVED;
        this.seniorManagementId = seniorMgmtId;
        addVersion("Plan approved and finalized by Senior Management", seniorMgmtId);
    }

    public void rejectBySeniorManagement(UUID seniorMgmtId) {
        if (this.status != AuditPlanStatus.SUBMITTED_TO_SENIOR_MGMT) {
            throw new IllegalStateException("Plan must be in SUBMITTED_TO_SENIOR_MGMT state to reject.");
        }
        this.status = AuditPlanStatus.RETURNED_TO_DIRECTOR;
        addVersion("Plan returned to Director by Senior Management", seniorMgmtId);
    }

    public void incrementCasesCreated() {
        if (this.status != AuditPlanStatus.APPROVED) {
            throw new IllegalStateException("Cases can only be created from an APPROVED annual plan.");
        }
        this.totalCasesCreated++;
    }

    private void addVersion(String summary, UUID actorId) {
        int nextVersion = this.versions.size() + 1;
        PlanVersion version = PlanVersion.builder()
                .versionNumber(nextVersion)
                .changeSummary(summary)
                .changedBy(actorId)
                .changedAt(Instant.now())
                .snapshot(takeSnapshot())
                .build();
        this.versions.add(version);
    }

    private String takeSnapshot() {
        return String.format("{\"year\":%d,\"status\":\"%s\",\"totalCasesPlanned\":%d,\"totalCasesCreated\":%d,\"plannedVolumeByType\":%s}",
                year, status.name(), totalCasesPlanned, totalCasesCreated, plannedVolumeByType.toString());
    }
}

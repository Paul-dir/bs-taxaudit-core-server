package com.mor.itas.planning.api.dto.response;

import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import lombok.Data;

@Data
public class AnnualAuditPlanResponse {
    private AnnualAuditPlan plan;

    public AnnualAuditPlanResponse(AnnualAuditPlan plan) {
        this.plan = plan;
    }
}

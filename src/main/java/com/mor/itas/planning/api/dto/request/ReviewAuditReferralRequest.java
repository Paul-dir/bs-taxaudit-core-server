package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class ReviewAuditReferralRequest {
    private boolean accept;
    private UUID actorId;
    private String reason;
    private UUID annualPlanId;
}

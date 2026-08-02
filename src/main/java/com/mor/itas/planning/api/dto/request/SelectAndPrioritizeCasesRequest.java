package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SelectAndPrioritizeCasesRequest {
    private UUID annualPlanId;
    private String branch;
    private String segment;
    private String auditTypeStr;
    private BigDecimal riskMin;
    private int randomSampleCount;
}

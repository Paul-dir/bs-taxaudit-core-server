package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RiskEnginePort {
    List<RiskProfile> getRiskRankedPool(String branch, String taxpayerSegment, String auditType, BigDecimal riskScoreMin);
    RiskProfile getRiskScore(String tin);
    ComplexityScore getComplexityScore(UUID caseId);

    @Value
    @Builder
    class RiskProfile {
        String tin;
        BigDecimal riskScore;
        RiskLevel riskLevel;
        List<String> riskIndicators;
    }

    @Value
    @Builder
    class ComplexityScore {
        UUID caseId;
        BigDecimal score;
        String complexityRating; // LOW, MEDIUM, HIGH
    }
}

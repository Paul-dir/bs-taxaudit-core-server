package com.mor.itas.planning.engineadapter.risk;

import com.itas.bs.taxaudit.application.port.RiskEnginePort;
import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class RiskEngineAdapter implements RiskEnginePort {
    @Override
    public List<RiskProfile> getRiskRankedPool(String branch, String taxpayerSegment, String auditType, BigDecimal riskScoreMin) {
        return Collections.emptyList(); // Stub for external service
    }

    @Override
    public RiskProfile getRiskScore(String tin) {
        return RiskProfile.builder()
                .tin(tin)
                .riskScore(new BigDecimal("50.0"))
                .riskLevel(RiskLevel.MEDIUM)
                .riskIndicators(Collections.emptyList())
                .build();
    }

    @Override
    public ComplexityScore getComplexityScore(UUID caseId) {
        return ComplexityScore.builder()
                .caseId(caseId)
                .score(new BigDecimal("70.0"))
                .complexityRating("MEDIUM")
                .build();
    }
}

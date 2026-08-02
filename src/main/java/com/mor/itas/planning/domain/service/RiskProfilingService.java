package com.mor.itas.planning.domain.service;

import com.itas.bs.taxaudit.application.port.RiskEnginePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskProfilingService {
    private final RiskEnginePort riskEnginePort;

    public List<RiskEnginePort.RiskProfile> getRiskRankedPool(String branch, String taxpayerSegment, String auditType, BigDecimal riskScoreMin) {
        return riskEnginePort.getRiskRankedPool(branch, taxpayerSegment, auditType, riskScoreMin);
    }

    public RiskEnginePort.RiskProfile getRiskScore(String tin) {
        return riskEnginePort.getRiskScore(tin);
    }

    public RiskEnginePort.ComplexityScore getComplexityScore(UUID caseId) {
        return riskEnginePort.getComplexityScore(caseId);
    }
}

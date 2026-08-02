package com.mor.itas.planning.domain.service;

import com.itas.bs.taxaudit.application.port.RiskEnginePort;
import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RiskAssessorService {
    private final RiskProfilingService riskProfilingService;

    public RiskLevel assessRiskLevel(String tin) {
        RiskEnginePort.RiskProfile profile = riskProfilingService.getRiskScore(tin);
        if (profile == null) {
            return RiskLevel.LOW;
        }
        BigDecimal score = profile.getRiskScore();
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return RiskLevel.CRITICAL;
        } else if (score.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return RiskLevel.HIGH;
        } else if (score.compareTo(BigDecimal.valueOf(30)) >= 0) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }
}

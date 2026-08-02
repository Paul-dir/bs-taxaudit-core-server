package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.application.port.RiskEnginePort;
import com.itas.bs.taxaudit.domain.event.AuditCaseCreatedEvent;
import com.itas.bs.taxaudit.domain.event.AuditCaseSelectedEvent;
import com.itas.bs.taxaudit.domain.event.RandomAuditCaseSelectedEvent;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.domain.service.RiskProfilingService;
import com.itas.bs.taxaudit.domain.service.SamplingService;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseStatus;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SelectAndPrioritizeAuditCasesUseCase {
    private final RiskProfilingService riskProfilingService;
    private final SamplingService samplingService;
    private final AuditCaseRepositoryPort caseRepository;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID annualPlanId, String branch, String segment, String auditTypeStr, BigDecimal riskMin, int randomSampleCount) {
        // 1. Pull Risk-Ranked pool via Rule 2
        List<RiskEnginePort.RiskProfile> riskPool = riskProfilingService.getRiskRankedPool(branch, segment, auditTypeStr, riskMin);
        
        // 2. Select risk-based cases
        for (RiskEnginePort.RiskProfile profile : riskPool) {
            createAndSelectCase(profile, annualPlanId, AuditCaseSource.RISK_ENGINE);
        }
        
        // 3. Draw random sample for feedback loop (Rule 12)
        if (randomSampleCount > 0) {
            List<String> poolTins = riskPool.stream().map(RiskEnginePort.RiskProfile::getTin).collect(Collectors.toList());
            List<String> randomTins = samplingService.drawRandomSample(poolTins, randomSampleCount);
            
            for (String tin : randomTins) {
                // Find matching profile for standard creation
                RiskEnginePort.RiskProfile profile = riskPool.stream().filter(p -> p.getTin().equals(tin)).findFirst().orElse(null);
                if (profile != null) {
                    createAndSelectCase(profile, annualPlanId, AuditCaseSource.RANDOM_SAMPLE);
                }
            }
        }
    }

    private void createAndSelectCase(RiskEnginePort.RiskProfile profile, UUID annualPlanId, AuditCaseSource source) {
        String caseReference = "AC-" + profile.getTin() + "-" + System.currentTimeMillis();
        AuditCase newCase = AuditCase.builder()
                .id(UUID.randomUUID())
                .caseReferenceNumber(caseReference)
                .status(AuditCaseStatus.CREATED)
                .taxpayerPartyId(UUID.randomUUID()) 
                .tin(profile.getTin())
                .auditType(AuditType.COMPREHENSIVE)
                .riskLevel(profile.getRiskLevel())
                .source(source)
                .annualPlanId(annualPlanId)
                .build();

        AuditCase savedCase = caseRepository.save(newCase);
        
        eventDispatchPort.dispatch(new AuditCaseCreatedEvent(
                savedCase.getId(), savedCase.getCaseReferenceNumber(), savedCase.getTin(), annualPlanId
        ));

        if (source == AuditCaseSource.RANDOM_SAMPLE) {
            eventDispatchPort.dispatch(new RandomAuditCaseSelectedEvent(savedCase.getId(), savedCase.getTin()));
        } else {
            eventDispatchPort.dispatch(new AuditCaseSelectedEvent(savedCase.getId(), savedCase.getTin()));
        }
    }
}

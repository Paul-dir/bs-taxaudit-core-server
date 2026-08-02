package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.AuditReferralRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.application.port.RegistrationServicePort;
import com.itas.bs.taxaudit.domain.event.AuditCaseCreatedEvent;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.domain.model.AuditReferral;
import com.itas.bs.taxaudit.domain.service.RiskAssessorService;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseStatus;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.RiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewAuditReferralUseCase {
    private final AuditReferralRepositoryPort referralRepository;
    private final AuditCaseRepositoryPort caseRepository;
    private final RegistrationServicePort registrationPort;
    private final RiskAssessorService riskAssessorService;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public void execute(UUID referralId, boolean accept, UUID actorId, String reason, UUID annualPlanId) {
        AuditReferral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new DomainException("Referral not found"));

        referral.triage(accept, actorId, reason);

        if (accept) {
            RegistrationServicePort.TaxpayerSummary taxpayer = registrationPort.lookupTaxpayer(referral.getRelatedTaxpayerTin());
            if (taxpayer == null) {
                throw new DomainException("Taxpayer not found in registry for TIN: " + referral.getRelatedTaxpayerTin());
            }

            RiskLevel assessedRisk = riskAssessorService.assessRiskLevel(taxpayer.getTin());

            String caseReference = "AC-" + referral.getRelatedTaxpayerTin() + "-" + System.currentTimeMillis();
            AuditCase newCase = AuditCase.builder()
                    .id(UUID.randomUUID())
                    .caseReferenceNumber(caseReference)
                    .status(AuditCaseStatus.CREATED)
                    .taxpayerPartyId(UUID.randomUUID()) // Mocking taxpayer UUID mapping
                    .tin(taxpayer.getTin())
                    .auditType(AuditType.COMPREHENSIVE) // Default assumption, to be refined later
                    .riskLevel(assessedRisk)
                    .source(referral.getSourceType())
                    .sourceReferralId(referral.getId())
                    .annualPlanId(annualPlanId)
                    .build();

            AuditCase savedCase = caseRepository.save(newCase);
            referral.resolve(savedCase.getId());

            AuditCaseCreatedEvent event = new AuditCaseCreatedEvent(
                    savedCase.getId(), savedCase.getCaseReferenceNumber(), savedCase.getTin(), annualPlanId
            );
            eventDispatchPort.dispatch(event);
        }

        referralRepository.save(referral);
    }
}

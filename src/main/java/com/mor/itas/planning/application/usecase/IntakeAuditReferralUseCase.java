package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditReferralRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.domain.event.AuditReferralReceivedEvent;
import com.itas.bs.taxaudit.domain.model.AuditReferral;
import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import com.itas.bs.taxaudit.domain.valueobject.ReferralStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IntakeAuditReferralUseCase {
    private final AuditReferralRepositoryPort referralRepository;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public UUID execute(AuditCaseSource source, String referringEntity, String referenceDetails, String relatedTaxpayerTin) {
        AuditReferral referral = AuditReferral.builder()
                .id(UUID.randomUUID())
                .sourceType(source)
                .referringEntity(referringEntity)
                .referenceDetails(referenceDetails)
                .relatedTaxpayerTin(relatedTaxpayerTin)
                .status(ReferralStatus.RECEIVED)
                .receivedAt(Instant.now())
                .build();

        AuditReferral savedReferral = referralRepository.save(referral);

        AuditReferralReceivedEvent event = new AuditReferralReceivedEvent(
                savedReferral.getId(), source, referringEntity, relatedTaxpayerTin
        );
        eventDispatchPort.dispatch(event);

        return savedReferral.getId();
    }
}

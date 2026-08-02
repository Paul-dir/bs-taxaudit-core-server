package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.model.AuditReferral;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditReferralRepositoryPort {
    AuditReferral save(AuditReferral referral);
    Optional<AuditReferral> findById(UUID id);
    Optional<AuditReferral> findByRelatedTaxpayerTin(String tin);
    List<AuditReferral> findAll();
}

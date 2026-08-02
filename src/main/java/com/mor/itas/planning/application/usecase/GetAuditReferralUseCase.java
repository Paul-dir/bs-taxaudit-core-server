package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditReferralRepositoryPort;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditReferral;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAuditReferralUseCase {
    private final AuditReferralRepositoryPort referralRepository;

    public AuditReferral getById(UUID id) {
        return referralRepository.findById(id)
                .orElseThrow(() -> new DomainException("Audit Referral not found"));
    }

    public List<AuditReferral> getAll() {
        return referralRepository.findAll();
    }
}

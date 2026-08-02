package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditReferralRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AuditReferral;
import com.itas.bs.taxaudit.domain.valueobject.ReferralStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPendingReferralsUseCase {
    private final AuditReferralRepositoryPort referralRepository;

    public List<AuditReferral> getPending() {
        return referralRepository.findAll().stream()
                .filter(ref -> ref.getStatus() == ReferralStatus.RECEIVED)
                .collect(Collectors.toList());
    }
}

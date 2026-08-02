package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAuditCaseUseCase {
    private final AuditCaseRepositoryPort caseRepository;

    public AuditCase getById(UUID id) {
        return caseRepository.findById(id)
                .orElseThrow(() -> new DomainException("Audit Case not found"));
    }

    public AuditCase getByReference(String referenceNumber) {
        return caseRepository.findByCaseReferenceNumber(referenceNumber)
                .orElseThrow(() -> new DomainException("Audit Case not found for reference: " + referenceNumber));
    }

    public List<AuditCase> getByTin(String tin) {
        return caseRepository.findByTin(tin);
    }
}

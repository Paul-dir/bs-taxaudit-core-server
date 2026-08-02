package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.model.AuditCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditCaseRepositoryPort {
    AuditCase save(AuditCase auditCase);
    Optional<AuditCase> findById(UUID id);
    Optional<AuditCase> findByCaseReferenceNumber(String caseReferenceNumber);
    List<AuditCase> findByTin(String tin);
    List<AuditCase> findAll();
    List<AuditCase> findUnassignedCases();
    List<AuditCase> findSelectedCases();
    List<AuditCase> findCasesInPool();
    long countByAnnualPlanId(UUID annualPlanId);
    List<AuditCase> findAllById(List<UUID> ids);
    void saveAll(List<AuditCase> cases);
}

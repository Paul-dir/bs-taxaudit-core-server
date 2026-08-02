package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.persistence.jpa.entity.AuditCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditCaseJpaRepository extends JpaRepository<AuditCaseEntity, UUID> {
    Optional<AuditCaseEntity> findByCaseReferenceNumber(String caseReferenceNumber);
    List<AuditCaseEntity> findByTin(String tin);
    long countByAnnualPlanId(UUID annualPlanId);
}

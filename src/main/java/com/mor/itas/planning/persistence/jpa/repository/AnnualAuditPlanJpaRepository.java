package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.persistence.jpa.entity.AnnualAuditPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnualAuditPlanJpaRepository extends JpaRepository<AnnualAuditPlanEntity, UUID> {
    Optional<AnnualAuditPlanEntity> findByPlanYear(int planYear);
}

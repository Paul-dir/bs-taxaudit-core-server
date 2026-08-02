package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnualAuditPlanRepositoryPort {
    AnnualAuditPlan save(AnnualAuditPlan plan);
    Optional<AnnualAuditPlan> findById(UUID id);
    Optional<AnnualAuditPlan> findByYear(int year);
    List<AnnualAuditPlan> findAll();
}

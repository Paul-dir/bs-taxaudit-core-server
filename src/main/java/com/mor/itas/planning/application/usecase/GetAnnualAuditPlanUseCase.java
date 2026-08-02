package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AnnualAuditPlanRepositoryPort;
import com.itas.bs.taxaudit.domain.exception.DomainException;
import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAnnualAuditPlanUseCase {
    private final AnnualAuditPlanRepositoryPort planRepository;

    public AnnualAuditPlan getById(UUID planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new DomainException("Annual Audit Plan not found"));
    }

    public AnnualAuditPlan getByYear(int year) {
        return planRepository.findByYear(year)
                .orElseThrow(() -> new DomainException("Annual Audit Plan for year " + year + " not found"));
    }

    public List<AnnualAuditPlan> getAllPlans() {
        return planRepository.findAll();
    }
}

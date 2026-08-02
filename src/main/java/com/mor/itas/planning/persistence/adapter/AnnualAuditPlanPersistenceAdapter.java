package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.AnnualAuditPlanRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import com.itas.bs.taxaudit.persistence.jpa.entity.AnnualAuditPlanEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.AnnualAuditPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnnualAuditPlanPersistenceAdapter implements AnnualAuditPlanRepositoryPort {
    private final AnnualAuditPlanJpaRepository repository;

    @Override
    public AnnualAuditPlan save(AnnualAuditPlan plan) {
        AnnualAuditPlanEntity entity = mapToEntity(plan);
        AnnualAuditPlanEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<AnnualAuditPlan> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<AnnualAuditPlan> findByYear(int year) {
        return repository.findByPlanYear(year).map(this::mapToDomain);
    }

    @Override
    public List<AnnualAuditPlan> findAll() {
        return repository.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    private AnnualAuditPlanEntity mapToEntity(AnnualAuditPlan domain) {
        AnnualAuditPlanEntity entity = new AnnualAuditPlanEntity();
        entity.setId(domain.getId());
        entity.setPlanYear(domain.getYear());
        entity.setStatus(domain.getStatus());
        entity.setTotalCasesPlanned(domain.getTotalCasesPlanned());
        entity.setTotalCasesCreated(domain.getTotalCasesCreated());
        entity.setDirectorId(domain.getDirectorId());
        return entity;
    }

    private AnnualAuditPlan mapToDomain(AnnualAuditPlanEntity entity) {
        return AnnualAuditPlan.builder()
                .id(entity.getId())
                .year(entity.getPlanYear())
                .status(entity.getStatus())
                .totalCasesPlanned(entity.getTotalCasesPlanned())
                .totalCasesCreated(entity.getTotalCasesCreated())
                .directorId(entity.getDirectorId())
                .build();
    }
}

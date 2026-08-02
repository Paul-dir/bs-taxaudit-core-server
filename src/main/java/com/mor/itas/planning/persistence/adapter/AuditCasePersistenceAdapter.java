package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.persistence.jpa.entity.AuditCaseEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.AuditCaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditCasePersistenceAdapter implements AuditCaseRepositoryPort {
    private final AuditCaseJpaRepository repository;

    @Override
    public AuditCase save(AuditCase auditCase) {
        AuditCaseEntity entity = mapToEntity(auditCase);
        return mapToDomain(repository.save(entity));
    }

    @Override
    public void saveAll(List<AuditCase> cases) {
        List<AuditCaseEntity> entities = cases.stream().map(this::mapToEntity).collect(Collectors.toList());
        repository.saveAll(entities);
    }

    @Override
    public List<AuditCase> findAllById(List<UUID> ids) {
        return repository.findAllById(ids).stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<AuditCase> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<AuditCase> findByCaseReferenceNumber(String caseReferenceNumber) {
        return repository.findByCaseReferenceNumber(caseReferenceNumber).map(this::mapToDomain);
    }

    @Override
    public List<AuditCase> findByTin(String tin) {
        return repository.findByTin(tin).stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditCase> findAll() {
        return repository.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public List<AuditCase> findUnassignedCases() {
        return repository.findAll().stream()
                .filter(e -> e.getAssignedAuditorId() == null)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditCase> findSelectedCases() {
        return repository.findAll().stream()
                .filter(e -> e.getStatus().name().equals("SELECTED_FOR_AUDIT"))
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditCase> findCasesInPool() {
        return repository.findAll().stream()
                .filter(e -> e.getStatus().name().equals("CREATED"))
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByAnnualPlanId(UUID annualPlanId) {
        return repository.countByAnnualPlanId(annualPlanId);
    }

    private AuditCaseEntity mapToEntity(AuditCase domain) {
        AuditCaseEntity entity = new AuditCaseEntity();
        entity.setId(domain.getId());
        entity.setCaseReferenceNumber(domain.getCaseReferenceNumber());
        entity.setStatus(domain.getStatus());
        entity.setTaxpayerPartyId(domain.getTaxpayerPartyId());
        entity.setTaxpayerName(domain.getTaxpayerName());
        entity.setTin(domain.getTin());
        entity.setRiskScore(domain.getRiskScore());
        entity.setRevenueAtRisk(domain.getRevenueAtRisk());
        entity.setEstimatedHours(domain.getEstimatedHours());
        entity.setAuditType(domain.getAuditType());
        entity.setRiskLevel(domain.getRiskLevel());
        entity.setSource(domain.getSource());
        entity.setSourceReferralId(domain.getSourceReferralId());
        entity.setAnnualPlanId(domain.getAnnualPlanId());
        entity.setAssignedAuditorId(domain.getAssignedAuditorId());
        entity.setTeamLeaderId(domain.getTeamLeaderId());
        return entity;
    }

    private AuditCase mapToDomain(AuditCaseEntity entity) {
        return AuditCase.builder()
                .id(entity.getId())
                .caseReferenceNumber(entity.getCaseReferenceNumber())
                .status(entity.getStatus())
                .taxpayerPartyId(entity.getTaxpayerPartyId())
                .taxpayerName(entity.getTaxpayerName())
                .tin(entity.getTin())
                .riskScore(entity.getRiskScore())
                .revenueAtRisk(entity.getRevenueAtRisk())
                .estimatedHours(entity.getEstimatedHours())
                .auditType(entity.getAuditType())
                .riskLevel(entity.getRiskLevel())
                .source(entity.getSource())
                .sourceReferralId(entity.getSourceReferralId())
                .annualPlanId(entity.getAnnualPlanId())
                .assignedAuditorId(entity.getAssignedAuditorId())
                .teamLeaderId(entity.getTeamLeaderId())
                .build();
    }
}

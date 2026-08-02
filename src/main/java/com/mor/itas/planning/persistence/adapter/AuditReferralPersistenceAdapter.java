package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.AuditReferralRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AuditReferral;
import com.itas.bs.taxaudit.persistence.jpa.entity.AuditReferralEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.AuditReferralJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuditReferralPersistenceAdapter implements AuditReferralRepositoryPort {
    private final AuditReferralJpaRepository repository;

    @Override
    public AuditReferral save(AuditReferral referral) {
        AuditReferralEntity entity = mapToEntity(referral);
        return mapToDomain(repository.save(entity));
    }

    @Override
    public Optional<AuditReferral> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<AuditReferral> findByRelatedTaxpayerTin(String tin) {
        return repository.findByRelatedTaxpayerTin(tin).map(this::mapToDomain);
    }

    @Override
    public List<AuditReferral> findAll() {
        return repository.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    private AuditReferralEntity mapToEntity(AuditReferral domain) {
        AuditReferralEntity entity = new AuditReferralEntity();
        entity.setId(domain.getId());
        entity.setSourceType(domain.getSourceType());
        entity.setReferringEntity(domain.getReferringEntity());
        entity.setReferenceDetails(domain.getReferenceDetails());
        entity.setRelatedTaxpayerTin(domain.getRelatedTaxpayerTin());
        entity.setStatus(domain.getStatus());
        entity.setReceivedAt(domain.getReceivedAt());
        entity.setResolvedCaseId(domain.getResolvedCaseId());
        return entity;
    }

    private AuditReferral mapToDomain(AuditReferralEntity entity) {
        return AuditReferral.builder()
                .id(entity.getId())
                .sourceType(entity.getSourceType())
                .referringEntity(entity.getReferringEntity())
                .referenceDetails(entity.getReferenceDetails())
                .relatedTaxpayerTin(entity.getRelatedTaxpayerTin())
                .status(entity.getStatus())
                .receivedAt(entity.getReceivedAt())
                .resolvedCaseId(entity.getResolvedCaseId())
                .build();
    }
}

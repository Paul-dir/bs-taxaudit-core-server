package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.persistence.jpa.entity.AuditReferralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditReferralJpaRepository extends JpaRepository<AuditReferralEntity, UUID> {
    Optional<AuditReferralEntity> findByRelatedTaxpayerTin(String relatedTaxpayerTin);
}

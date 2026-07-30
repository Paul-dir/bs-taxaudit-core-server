package com.act.taxaudit.persistence.jpa.repository;

import com.act.taxaudit.persistence.jpa.entity.DeskAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeskAuditJpaRepository extends JpaRepository<DeskAuditEntity, UUID> {
    List<DeskAuditEntity> findByAuditCaseId(UUID auditCaseId);
    List<DeskAuditEntity> findByStatus(String status);

    @Query("SELECT d FROM DeskAuditEntity d WHERE d.status NOT IN ('FINALIZED', 'ESCALATED_TO_COMPREHENSIVE', 'SUSPENDED_FRAUD_INVESTIGATION')")
    List<DeskAuditEntity> findAllOpen();
}
package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.persistence.jpa.entity.AssignmentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentLogJpaRepository extends JpaRepository<AssignmentLogEntity, UUID> {
    List<AssignmentLogEntity> findByAuditCaseId(UUID auditCaseId);
}

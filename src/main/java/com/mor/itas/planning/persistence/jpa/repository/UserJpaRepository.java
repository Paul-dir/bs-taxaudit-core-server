package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.Role;
import com.itas.bs.taxaudit.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findByRoleAndAssignedTaxCenterAndAuditType(Role role, String assignedTaxCenter, AuditType auditType);
    List<UserEntity> findByTeamId(String teamId);
}

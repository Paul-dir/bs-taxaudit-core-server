package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.model.User;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findById(UUID id);
    List<User> findByRoleAndTaxCenterAndAuditType(Role role, String taxCenter, AuditType auditType);
    List<User> findByTeamId(String teamId);
    void save(User user);
    void saveAll(List<User> users);
}

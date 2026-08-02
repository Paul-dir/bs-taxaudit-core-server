package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.UserRepositoryPort;
import com.itas.bs.taxaudit.domain.model.User;
import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.OrgContext;
import com.itas.bs.taxaudit.domain.valueobject.Role;
import com.itas.bs.taxaudit.persistence.jpa.entity.UserEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository repository;

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<User> findByRoleAndTaxCenterAndAuditType(Role role, String taxCenter, AuditType auditType) {
        return repository.findByRoleAndAssignedTaxCenterAndAuditType(role, taxCenter, auditType)
                .stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findByTeamId(String teamId) {
        return repository.findByTeamId(teamId).stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        repository.save(mapToEntity(user));
    }

    @Override
    public void saveAll(List<User> users) {
        repository.saveAll(users.stream().map(this::mapToEntity).collect(Collectors.toList()));
    }

    private UserEntity mapToEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setCurrentWorkload(domain.getCurrentWorkload());
        entity.setMaxCapacity(domain.getMaxCapacity());
        if (domain.getOrgContext() != null) {
            entity.setAssignedRegion(domain.getOrgContext().getAssignedRegion());
            entity.setAssignedTaxCenter(domain.getOrgContext().getAssignedTaxCenter());
            entity.setAuditType(domain.getOrgContext().getAuditType());
            entity.setTeamId(domain.getOrgContext().getTeamId());
        }
        return entity;
    }

    private User mapToDomain(UserEntity entity) {
        OrgContext orgContext = OrgContext.builder()
                .assignedRegion(entity.getAssignedRegion())
                .assignedTaxCenter(entity.getAssignedTaxCenter())
                .auditType(entity.getAuditType())
                .teamId(entity.getTeamId())
                .build();

        return User.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .status(entity.getStatus())
                .orgContext(orgContext)
                .currentWorkload(entity.getCurrentWorkload())
                .maxCapacity(entity.getMaxCapacity())
                .build();
    }
}

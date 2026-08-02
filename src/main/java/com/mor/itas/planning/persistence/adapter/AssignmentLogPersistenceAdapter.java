package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.AssignmentLogRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AssignmentLog;
import com.itas.bs.taxaudit.persistence.jpa.entity.AssignmentLogEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.AssignmentLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssignmentLogPersistenceAdapter implements AssignmentLogRepositoryPort {

    private final AssignmentLogJpaRepository repository;

    @Override
    public void save(AssignmentLog log) {
        repository.save(mapToEntity(log));
    }

    @Override
    public void saveAll(List<AssignmentLog> logs) {
        repository.saveAll(logs.stream().map(this::mapToEntity).collect(Collectors.toList()));
    }

    @Override
    public List<AssignmentLog> findByAuditCaseId(UUID caseId) {
        return repository.findByAuditCaseId(caseId).stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    private AssignmentLogEntity mapToEntity(AssignmentLog domain) {
        AssignmentLogEntity entity = new AssignmentLogEntity();
        entity.setId(domain.getId());
        entity.setAuditCaseId(domain.getAuditCaseId());
        entity.setFromUserId(domain.getFromUserId());
        entity.setToUserId(domain.getToUserId());
        entity.setReason(domain.getReason());
        entity.setStatusTransition(domain.getStatusTransition());
        entity.setTransitionDate(domain.getTransitionDate());
        return entity;
    }

    private AssignmentLog mapToDomain(AssignmentLogEntity entity) {
        return AssignmentLog.builder()
                .id(entity.getId())
                .auditCaseId(entity.getAuditCaseId())
                .fromUserId(entity.getFromUserId())
                .toUserId(entity.getToUserId())
                .reason(entity.getReason())
                .statusTransition(entity.getStatusTransition())
                .transitionDate(entity.getTransitionDate())
                .build();
    }
}

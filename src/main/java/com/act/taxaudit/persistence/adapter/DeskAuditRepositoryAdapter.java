package com.act.taxaudit.persistence.adapter;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.persistence.jpa.entity.DeskAuditEntity;
import com.act.taxaudit.persistence.jpa.repository.DeskAuditJpaRepository;
import com.act.taxaudit.persistence.mapper.DeskAuditMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DeskAuditRepositoryAdapter implements DeskAuditRepositoryPort {

    private final DeskAuditJpaRepository jpaRepository;
    private final DeskAuditMapper mapper;

    public DeskAuditRepositoryAdapter(DeskAuditJpaRepository jpaRepository, DeskAuditMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DeskAudit save(DeskAudit deskAudit) {
        DeskAuditEntity entity = mapper.toEntity(deskAudit);
        DeskAuditEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<DeskAudit> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DeskAudit> findByAuditCaseId(UUID auditCaseId) {
        return jpaRepository.findByAuditCaseId(auditCaseId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<DeskAudit> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<DeskAudit> findAllOpenWithPendingDocumentRequests() {
        return jpaRepository.findAllOpen().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}
package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetDeskAuditUseCase {

    private final DeskAuditRepositoryPort repository;

    public GetDeskAuditUseCase(DeskAuditRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public DeskAudit execute(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("DeskAudit", id));
    }
}
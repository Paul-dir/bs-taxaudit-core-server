package com.act.taxaudit.application.usecase;

import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FinalizeDeskAuditUseCase {

    private final DeskAuditRepositoryPort repository;

    public FinalizeDeskAuditUseCase(DeskAuditRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, String finalizedByActorId) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new com.act.taxaudit.domain.exception.ResourceNotFoundException("Desk audit not found: " + deskAuditId));
        deskAudit.finalize(finalizedByActorId);
        return repository.save(deskAudit);
    }
}
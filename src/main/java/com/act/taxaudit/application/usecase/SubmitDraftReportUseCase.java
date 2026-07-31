package com.act.taxaudit.application.usecase;

import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.valueobject.DraftAuditReport;
import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SubmitDraftReportUseCase {

    private final DeskAuditRepositoryPort repository;

    public SubmitDraftReportUseCase(DeskAuditRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, DraftAuditReport report, String preparedByActorId) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new com.act.taxaudit.domain.exception.ResourceNotFoundException("Desk audit not found: " + deskAuditId));
        deskAudit.submitDraftReport(report, preparedByActorId);
        return repository.save(deskAudit);
    }
}
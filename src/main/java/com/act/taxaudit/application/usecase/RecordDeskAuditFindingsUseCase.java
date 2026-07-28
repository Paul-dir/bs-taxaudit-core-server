package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import com.act.taxaudit.domain.valueobject.DeskAuditFinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RecordDeskAuditFindingsUseCase {

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    public RecordDeskAuditFindingsUseCase(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, List<DeskAuditFinding> findings) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new ResourceNotFoundException("DeskAudit", deskAuditId));

        deskAudit.recordFindings(findings);

        DeskAudit saved = repository.save(deskAudit);
        saved.pullEvents().forEach(eventPublisher::publish);
        return saved;
    }
}
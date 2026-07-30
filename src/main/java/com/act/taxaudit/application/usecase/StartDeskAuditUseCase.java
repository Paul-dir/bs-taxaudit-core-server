package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StartDeskAuditUseCase {

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    public StartDeskAuditUseCase(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DeskAudit execute(UUID auditCaseId, String tin) {
        DeskAudit deskAudit = DeskAudit.start(auditCaseId, tin);
        DeskAudit saved = repository.save(deskAudit);
        saved.pullEvents().forEach(eventPublisher::publish);
        return saved;
    }
}




package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import com.act.taxaudit.domain.valueobject.SampleSelection;
import com.act.taxaudit.domain.valueobject.SamplingMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DetermineSamplingMethodUseCase {

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    public DetermineSamplingMethodUseCase(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, SamplingMethod method, SampleSelection selection) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new ResourceNotFoundException("DeskAudit", deskAuditId));

        deskAudit.determineSampling(method, selection);

        DeskAudit saved = repository.save(deskAudit);
        saved.pullEvents().forEach(eventPublisher::publish);
        return saved;
    }
}
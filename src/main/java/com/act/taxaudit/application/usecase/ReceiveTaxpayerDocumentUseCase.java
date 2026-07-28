package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.DmsPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import com.act.taxaudit.domain.model.EvidenceItem;
import com.act.taxaudit.domain.valueobject.EvidenceSourceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReceiveTaxpayerDocumentUseCase {

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;
    private final DmsPort dmsPort;

    public ReceiveTaxpayerDocumentUseCase(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher, DmsPort dmsPort) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.dmsPort = dmsPort;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, byte[] documentContent, String fileName, String mimeType, String uploadedByActorId) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new ResourceNotFoundException("DeskAudit", deskAuditId));

        String documentRef = dmsPort.storeDocument(deskAuditId.toString(), documentContent, fileName, mimeType);

        EvidenceItem upload = new EvidenceItem(
            UUID.randomUUID().toString(),
            EvidenceSourceType.TAXPAYER_UPLOAD,
            "Taxpayer upload: " + fileName,
            documentRef,
            LocalDateTime.now(),
            uploadedByActorId
        );

        deskAudit.receiveTaxpayerUpload(upload);

        DeskAudit saved = repository.save(deskAudit);
        saved.pullEvents().forEach(eventPublisher::publish);
        return saved;
    }
}
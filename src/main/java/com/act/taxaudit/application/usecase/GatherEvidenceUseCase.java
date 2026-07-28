package com.act.taxaudit.application.usecase;

import com.act.taxaudit.application.port.DataWarehousePort;
import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.application.port.ThirdPartyDataPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.exception.ResourceNotFoundException;
import com.act.taxaudit.domain.model.EvidenceItem;
import com.act.taxaudit.domain.valueobject.EvidenceSourceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class GatherEvidenceUseCase {

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;
    private final DataWarehousePort dataWarehousePort;
    private final ThirdPartyDataPort thirdPartyDataPort;

    public GatherEvidenceUseCase(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher,
                                 DataWarehousePort dataWarehousePort, ThirdPartyDataPort thirdPartyDataPort) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.dataWarehousePort = dataWarehousePort;
        this.thirdPartyDataPort = thirdPartyDataPort;
    }

    @Transactional
    public DeskAudit execute(UUID deskAuditId, String actorId) {
        DeskAudit deskAudit = repository.findById(deskAuditId)
            .orElseThrow(() -> new ResourceNotFoundException("DeskAudit", deskAuditId));

        // Pull internal data from data warehouse
        Map<String, Object> internalData = dataWarehousePort.pullTaxpayerData(deskAudit.getTin());
        String internalEvidenceId = UUID.randomUUID().toString();
        EvidenceItem internalEvidence = new EvidenceItem(
            internalEvidenceId,
            EvidenceSourceType.INTERNAL_SYSTEM,
            "Internal data pulled from data warehouse: " + internalData.toString(),
            null,
            LocalDateTime.now(),
            actorId
        );
        deskAudit.recordEvidence(internalEvidence);

        // Pull third-party data
        Map<String, Object> thirdPartyData = thirdPartyDataPort.pullThirdPartyData(deskAudit.getTin());
        String thirdPartyEvidenceId = UUID.randomUUID().toString();
        EvidenceItem thirdPartyEvidence = new EvidenceItem(
            thirdPartyEvidenceId,
            EvidenceSourceType.THIRD_PARTY,
            "Third-party data: " + thirdPartyData.toString(),
            null,
            LocalDateTime.now(),
            actorId
        );
        deskAudit.recordEvidence(thirdPartyEvidence);

        DeskAudit saved = repository.save(deskAudit);
        saved.pullEvents().forEach(eventPublisher::publish);
        return saved;
    }
}
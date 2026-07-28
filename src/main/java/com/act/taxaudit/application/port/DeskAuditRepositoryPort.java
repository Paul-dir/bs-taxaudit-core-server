package com.act.taxaudit.application.port;

import com.act.taxaudit.domain.aggregate.DeskAudit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for DeskAudit aggregate persistence.
 */
public interface DeskAuditRepositoryPort {
    DeskAudit save(DeskAudit deskAudit);
    Optional<DeskAudit> findById(UUID id);
    List<DeskAudit> findByAuditCaseId(UUID auditCaseId);
    List<DeskAudit> findByStatus(String status);
    List<DeskAudit> findAllOpenWithPendingDocumentRequests();
}

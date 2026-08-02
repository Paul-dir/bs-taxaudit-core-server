package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.model.AssignmentLog;

import java.util.List;
import java.util.UUID;

public interface AssignmentLogRepositoryPort {
    void save(AssignmentLog log);
    void saveAll(List<AssignmentLog> logs);
    List<AssignmentLog> findByAuditCaseId(UUID caseId);
}

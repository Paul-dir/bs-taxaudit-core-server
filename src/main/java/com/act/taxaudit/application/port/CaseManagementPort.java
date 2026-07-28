package com.act.taxaudit.application.port;

/**
 * Port for Case Management system operations.
 */
public interface CaseManagementPort {
    void updateCaseStatus(java.util.UUID auditCaseId, String status);
}
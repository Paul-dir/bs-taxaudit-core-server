package com.act.taxaudit.application.port;

/**
 * Port for Fraud Investigation system operations.
 */
public interface FraudPort {
    void openFraudInvestigation(java.util.UUID deskAuditId, String tin, String notes);
}
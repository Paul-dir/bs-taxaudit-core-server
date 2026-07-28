package com.act.taxaudit.domain.valueobject;

/**
 * Desk Audit status enum representing the state machine.
 * Follows the pattern from bs-filling's TaxReturnStatus.
 */
public enum DeskAuditStatus {
    STARTED,
    EVIDENCE_GATHERING,
    DOCUMENTS_REQUESTED,
    SAMPLING_DETERMINED,
    FINDINGS_RECORDED,
    DRAFT_REPORT_SUBMITTED,
    FINALIZED,
    ESCALATED_TO_COMPREHENSIVE,
    SUSPENDED_FRAUD_INVESTIGATION
}
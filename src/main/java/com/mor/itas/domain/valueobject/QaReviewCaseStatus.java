package com.mor.itas.domain.valueobject;

/**
 * Lifecycle states for a QA review case.
 */
public enum QaReviewCaseStatus {
    DRAFT,              // Created but not yet submitted for review
    UNDER_REVIEW,       // Review in progress
    FINDINGS_RECORDED,  // Review complete, findings documented
    REPORT_GENERATED,   // QA report generated
    APPROVED,           // Report approved by authority
    REJECTED,           // Report rejected, needs revision
    CLOSED              // Case closed
}
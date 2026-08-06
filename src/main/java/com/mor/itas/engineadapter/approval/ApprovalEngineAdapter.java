package com.mor.itas.engineadapter.approval;

/**
 * Approval engine adapter for managing approval workflows.
 * Mock implementation for development/testing.
 */
public interface ApprovalEngineAdapter {
    void submitForApproval(String documentType, String documentId, String submittedBy);
    void approve(String approvalId, String approvedBy, String comments);
    void reject(String approvalId, String rejectedBy, String reason);
    boolean isApproved(String approvalId);
}
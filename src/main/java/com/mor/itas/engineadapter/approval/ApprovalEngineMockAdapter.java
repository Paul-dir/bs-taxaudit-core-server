package com.mor.itas.engineadapter.approval;

/**
 * Mock approval engine adapter for development and testing.
 */
public class ApprovalEngineMockAdapter implements ApprovalEngineAdapter {

    @Override
    public void submitForApproval(String documentType, String documentId, String submittedBy) {
        System.out.println("[ApprovalMock] Submitted for approval: " + documentType + ":" + documentId);
        System.out.println("  Submitted by: " + submittedBy);
    }

    @Override
    public void approve(String approvalId, String approvedBy, String comments) {
        System.out.println("[ApprovalMock] Approved: " + approvalId);
        System.out.println("  Approved by: " + approvedBy);
        System.out.println("  Comments: " + comments);
    }

    @Override
    public void reject(String approvalId, String rejectedBy, String reason) {
        System.out.println("[ApprovalMock] Rejected: " + approvalId);
        System.out.println("  Rejected by: " + rejectedBy);
        System.out.println("  Reason: " + reason);
    }

    @Override
    public boolean isApproved(String approvalId) {
        System.out.println("[ApprovalMock] Checking approval status for: " + approvalId);
        return true; // Mock always returns true
    }
}
package com.act.taxaudit.application.port;

/**
 * Port for Document Management System (DMS) operations.
 */
public interface DmsPort {
    String storeDocument(String deskAuditId, byte[] content, String fileName, String mimeType);
    byte[] fetchDocument(String documentReference);
}
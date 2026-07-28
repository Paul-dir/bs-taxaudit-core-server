package com.act.taxaudit.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when documents are requested from the taxpayer.
 */
public record DocumentsRequestedFromTaxpayerEvent(
    UUID eventId,
    Instant occurredAt,
    UUID deskAuditId,
    String tin,
    java.util.List<String> requestedDocumentTypes
) implements DomainEvent {

    public static DocumentsRequestedFromTaxpayerEvent of(UUID deskAuditId, String tin, java.util.List<String> requestedDocumentTypes) {
        return new DocumentsRequestedFromTaxpayerEvent(
            UUID.randomUUID(),
            Instant.now(),
            deskAuditId,
            tin,
            requestedDocumentTypes
        );
    }
}
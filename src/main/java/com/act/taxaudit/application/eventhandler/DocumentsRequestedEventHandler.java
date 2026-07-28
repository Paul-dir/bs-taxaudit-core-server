package com.act.taxaudit.application.eventhandler;

import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.application.port.NotificationEnginePort;
import com.act.taxaudit.domain.event.DocumentsRequestedFromTaxpayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DocumentsRequestedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DocumentsRequestedEventHandler.class);
    private static final String TOPIC = "taxaudit.deskaudit.documents-requested.v1";
    private static final String GROUP_ID = "taxaudit-documents-requested-handler";

    private final NotificationEnginePort notificationEngine;

    public DocumentsRequestedEventHandler(NotificationEnginePort notificationEngine) {
        this.notificationEngine = notificationEngine;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void handle(@Payload DocumentsRequestedFromTaxpayerEvent event) {
        log.info("Handling DocumentsRequestedFromTaxpayerEvent for deskAuditId={}", event.deskAuditId());
        try {
            Map<String, Object> variables = Map.of(
                "deskAuditId", event.deskAuditId().toString(),
                "tin", event.tin(),
                "requestedDocumentTypes", event.requestedDocumentTypes().toString()
            );
            notificationEngine.sendNotification(event.tin(), "DOCUMENTS_REQUESTED", variables);
            log.info("Notification sent for deskAuditId={}", event.deskAuditId());
        } catch (Exception ex) {
            log.error("Failed to handle DocumentsRequestedFromTaxpayerEvent for deskAuditId={}: {}",
                event.deskAuditId(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
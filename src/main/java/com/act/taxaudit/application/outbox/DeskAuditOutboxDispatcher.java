package com.act.taxaudit.application.outbox;

import com.act.taxaudit.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Scheduled outbox dispatcher that relays outbox entries to Kafka topics.
 * This replaces the bs-filling pattern of WebClient direct calls with Kafka publishing.
 */
@Component
public class DeskAuditOutboxDispatcher {

    private static final Logger log = LoggerFactory.getLogger(DeskAuditOutboxDispatcher.class);
    private static final int MAX_ATTEMPTS = 5;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DeskAuditOutboxDispatcher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${itas.outbox.poll-interval-ms:5000}")
    public void dispatchPendingEvents() {
        // This method would be implemented with the outbox repository integration
        // For now it's a stub demonstrating the Kafka publishing pattern
        log.debug("Outbox dispatch cycle executed");
    }

    /**
     * Publish a domain event to the appropriate Kafka topic.
     *
     * @param aggregateId the aggregate ID (used as partition key)
     * @param topic the Kafka topic
     * @param payload the event payload
     */
    public void publishToKafka(String aggregateId, String topic, Object payload) {
        CompletableFuture<SendResult<String, Object>> future =
            kafkaTemplate.send(topic, aggregateId, payload);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Published event to topic {} with key {} at offset {}",
                    topic, aggregateId, result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event to topic {} with key {}: {}",
                    topic, aggregateId, ex.getMessage(), ex);
            }
        });
    }

    /**
     * Determine the Kafka topic for a given event type.
     */
    public static String resolveTopic(String eventType) {
        return switch (eventType) {
            case "DocumentsRequestedFromTaxpayerEvent" -> KafkaConfig.TOPIC_DOCUMENTS_REQUESTED;
            case "ReminderSentEvent" -> KafkaConfig.TOPIC_REMINDER;
            case "DraftAuditReportSubmittedEvent" -> KafkaConfig.TOPIC_DRAFT_REPORT_READY;
            case "RiskProfileUpdateRequestedEvent" -> KafkaConfig.TOPIC_RISK_PROFILE_UPDATE;
            case "DeskAuditEscalatedToComprehensiveEvent" -> KafkaConfig.TOPIC_CASE_STATUS_CHANGED;
            case "FraudIndicatorFlaggedEvent" -> KafkaConfig.TOPIC_FRAUD_FLAGGED;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}
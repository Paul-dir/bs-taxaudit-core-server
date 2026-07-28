package com.act.taxaudit.application.job;

import com.act.taxaudit.application.port.DeskAuditRepositoryPort;
import com.act.taxaudit.application.port.EventPublisherPort;
import com.act.taxaudit.domain.aggregate.DeskAudit;
import com.act.taxaudit.domain.event.ReminderSentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Scheduled job that scans for overdue document requests and emits reminder events.
 */
@Component
public class DocumentRequestReminderJob {

    private static final Logger log = LoggerFactory.getLogger(DocumentRequestReminderJob.class);
    private static final String REMINDER_TEMPLATE = "DOCUMENT_REQUEST_REMINDER";

    private final DeskAuditRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    public DocumentRequestReminderJob(DeskAuditRepositoryPort repository, EventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "${itas.reminder.poll-interval-ms:60000}")
    public void sendReminders() {
        log.debug("Document request reminder job started");
        try {
            for (DeskAudit deskAudit : repository.findAllOpenWithPendingDocumentRequests()) {
                if (deskAudit.hasOverdueDocumentRequests()) {
                    ReminderSentEvent event = ReminderSentEvent.of(
                        deskAudit.getId(),
                        deskAudit.getTin(),
                        REMINDER_TEMPLATE,
                        Instant.now().plus(java.time.Duration.ofDays(3))
                    );
                    eventPublisher.publish(event);
                    log.info("Sent reminder for deskAuditId={}", deskAudit.getId());
                }
            }
        } catch (Exception ex) {
            log.error("Document request reminder job failed", ex);
        }
        log.debug("Document request reminder job finished");
    }
}
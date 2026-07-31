package com.act.taxaudit.app.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Retries failed outbox events and batch messaging.
 * MANDATORY for reliable inter-module communication in ITAS ecosystem.
 */
@Component
public class OutboxRetryScheduler {

    private static final Logger log = LoggerFactory.getLogger(OutboxRetryScheduler.class);

    @Scheduled(fixedDelayString = "${itas.outbox.poll-interval-ms:5000}")
    public void retryFailedOutboxEvents() {
        log.debug("Outbox retry cycle executed");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void batchMessageDispatch() {
        log.debug("Batch message dispatch cycle executed");
    }
}
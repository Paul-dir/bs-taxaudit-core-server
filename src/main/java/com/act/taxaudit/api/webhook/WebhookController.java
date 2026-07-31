package com.act.taxaudit.api.webhook;

import com.act.taxaudit.api.dto.response.ModuleCompletionNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Receives async completion notifications from other ITAS modules.
 * Also exposes endpoint for other modules to check Tax Audit module status.
 * MANDATORY for inter-module communication in ITAS ecosystem.
 */
@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/module-completion")
    public ResponseEntity<String> handleModuleCompletion(@RequestBody ModuleCompletionNotification notification) {
        log.info("Received module completion notification: module={} eventType={} aggregateId={} status={}",
                notification.module(), notification.eventType(), notification.aggregateId(), notification.status());

        return ResponseEntity.ok("ACK");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Tax Audit module is running");
    }
}
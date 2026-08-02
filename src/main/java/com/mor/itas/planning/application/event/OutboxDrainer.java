package com.mor.itas.planning.application.event;

import com.itas.bs.taxaudit.application.port.OutboxReadPort;
import com.itas.bs.taxaudit.persistence.adapter.OutboxPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxDrainer {
    private final OutboxReadPort outboxReadPort;
    private final OutboxPublisher outboxPublisher;

    @Scheduled(fixedDelay = 5000)
    public void drainOutbox() {
        List<OutboxReadPort.OutboxEventData> events = outboxReadPort.findUnpublishedEvents(100);
        
        for (OutboxReadPort.OutboxEventData event : events) {
            try {
                outboxPublisher.publishToBroker(event.getEventType(), event.getAggregateId().toString(), event.getPayload());
                outboxReadPort.markAsPublished(event.getId());
            } catch (Exception e) {
                log.error("Failed to publish outbox event: {}", event.getId(), e);
                // Stop processing further events to maintain ordering
                break;
            }
        }
    }
}

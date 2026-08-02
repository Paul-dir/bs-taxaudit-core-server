package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.OutboxReadPort;
import com.itas.bs.taxaudit.persistence.jpa.repository.OutboxEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OutboxReadPersistenceAdapter implements OutboxReadPort {
    private final OutboxEventJpaRepository outboxRepository;

    @Override
    public List<OutboxEventData> findUnpublishedEvents(int limit) {
        return outboxRepository.findByPublishedFalseOrderByCreatedAtAsc(PageRequest.of(0, limit))
                .stream()
                .map(e -> OutboxEventData.builder()
                        .id(e.getId())
                        .aggregateType(e.getAggregateType())
                        .aggregateId(e.getAggregateId())
                        .eventType(e.getEventType())
                        .payload(e.getPayload())
                        .createdAt(e.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsPublished(UUID eventId) {
        outboxRepository.findById(eventId).ifPresent(e -> {
            e.setPublished(true);
            e.setPublishedAt(Instant.now());
            outboxRepository.save(e);
        });
    }
}

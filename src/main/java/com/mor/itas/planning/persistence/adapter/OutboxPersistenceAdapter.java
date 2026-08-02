package com.mor.itas.planning.persistence.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itas.bs.taxaudit.application.port.OutboxPort;
import com.itas.bs.taxaudit.domain.event.DomainEvent;
import com.itas.bs.taxaudit.persistence.jpa.entity.OutboxEventEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.OutboxEventJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxPersistenceAdapter implements OutboxPort {
    private final OutboxEventJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    @Transactional(propagation = Propagation.MANDATORY) // Must participate in existing transaction
    public void save(DomainEvent event) {
        OutboxEventEntity entity = new OutboxEventEntity();
        entity.setId(event.getEventId());
        entity.setAggregateType(event.getClass().getSimpleName()); // Basic resolution
        entity.setAggregateId(event.getAggregateId());
        entity.setEventType(event.getEventType().name());
        entity.setPayload(objectMapper.writeValueAsString(event));
        entity.setCreatedAt(event.getOccurredAt());
        entity.setPublished(false);

        outboxRepository.save(entity);
    }
}

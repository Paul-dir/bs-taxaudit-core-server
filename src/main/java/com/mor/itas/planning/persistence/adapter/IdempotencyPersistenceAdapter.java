package com.mor.itas.planning.persistence.adapter;

import com.itas.bs.taxaudit.application.port.IdempotencyStorePort;
import com.itas.bs.taxaudit.persistence.jpa.entity.IdempotencyRecordEntity;
import com.itas.bs.taxaudit.persistence.jpa.repository.IdempotencyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class IdempotencyPersistenceAdapter implements IdempotencyStorePort {
    private final IdempotencyJpaRepository repository;

    @Override
    public boolean exists(String idempotencyKey) {
        return repository.existsById(idempotencyKey);
    }

    @Override
    public void save(String idempotencyKey, String responsePayload) {
        IdempotencyRecordEntity entity = new IdempotencyRecordEntity();
        entity.setIdempotencyKey(idempotencyKey);
        entity.setResponsePayload(responsePayload);
        entity.setCreatedAt(Instant.now());
        repository.save(entity);
    }
}

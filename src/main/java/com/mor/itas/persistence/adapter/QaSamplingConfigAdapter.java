package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.QaSamplingConfig;
import com.mor.itas.persistence.jpa.entity.QaSamplingConfigEntity;
import com.mor.itas.persistence.jpa.repository.QaSamplingConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaSamplingConfigAdapter {
    private final QaSamplingConfigRepository repository;

    public QaSamplingConfigAdapter(QaSamplingConfigRepository repository) {
        this.repository = repository;
    }

    public QaSamplingConfig save(QaSamplingConfig config) {
        QaSamplingConfigEntity entity = toEntity(config);
        QaSamplingConfigEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<QaSamplingConfig> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public List<QaSamplingConfig> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public Optional<QaSamplingConfig> findActiveConfig() {
        return repository.findAll().stream()
                .filter(QaSamplingConfigEntity::isActive)
                .findFirst()
                .map(this::toDomain);
    }

    private QaSamplingConfigEntity toEntity(QaSamplingConfig config) {
        QaSamplingConfigEntity entity = new QaSamplingConfigEntity();
        entity.setId(config.getId());
        entity.setSamplingMethod(config.getSamplingMethod());
        entity.setFrequency(config.getFrequency());
        entity.setScopeFilters(config.getScopeFilters());
        entity.setActive(config.isActive());
        return entity;
    }

    private QaSamplingConfig toDomain(QaSamplingConfigEntity entity) {
        QaSamplingConfig config = new QaSamplingConfig();
        config.setId(entity.getId());
        config.setSamplingMethod(entity.getSamplingMethod());
        config.setFrequency(entity.getFrequency());
        config.setScopeFilters(entity.getScopeFilters());
        config.setActive(entity.isActive());
        config.setCreatedAt(entity.getCreatedAt());
        config.setUpdatedAt(entity.getUpdatedAt());
        return config;
    }
}
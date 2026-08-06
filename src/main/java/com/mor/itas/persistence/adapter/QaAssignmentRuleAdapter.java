package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.QaAssignmentRule;
import com.mor.itas.persistence.jpa.entity.QaAssignmentRuleEntity;
import com.mor.itas.persistence.jpa.repository.QaAssignmentRuleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaAssignmentRuleAdapter {
    private final QaAssignmentRuleRepository repository;

    public QaAssignmentRuleAdapter(QaAssignmentRuleRepository repository) {
        this.repository = repository;
    }

    public QaAssignmentRule save(QaAssignmentRule rule) {
        QaAssignmentRuleEntity entity = toEntity(rule);
        QaAssignmentRuleEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<QaAssignmentRule> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public List<QaAssignmentRule> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public List<QaAssignmentRule> findActiveRules() {
        return repository.findAll().stream()
                .filter(QaAssignmentRuleEntity::isActive)
                .map(this::toDomain)
                .toList();
    }

    private QaAssignmentRuleEntity toEntity(QaAssignmentRule rule) {
        QaAssignmentRuleEntity entity = new QaAssignmentRuleEntity();
        entity.setId(rule.getId());
        entity.setRuleName(rule.getRuleName());
        entity.setCriteria(rule.getCriteria());
        entity.setPriority(rule.getPriority());
        entity.setActive(rule.isActive());
        return entity;
    }

    private QaAssignmentRule toDomain(QaAssignmentRuleEntity entity) {
        QaAssignmentRule rule = new QaAssignmentRule();
        rule.setId(entity.getId());
        rule.setRuleName(entity.getRuleName());
        rule.setCriteria(entity.getCriteria());
        rule.setPriority(entity.getPriority());
        rule.setActive(entity.isActive());
        rule.setCreatedAt(entity.getCreatedAt());
        rule.setUpdatedAt(entity.getUpdatedAt());
        return rule;
    }
}
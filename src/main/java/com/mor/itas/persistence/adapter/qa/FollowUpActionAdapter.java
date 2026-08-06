package com.mor.itas.persistence.adapter.qa;

import com.mor.itas.domain.model.FollowUpAction;
import com.mor.itas.persistence.jpa.entity.FollowUpActionEntity;
import com.mor.itas.persistence.jpa.repository.FollowUpActionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FollowUpActionAdapter {
    private final FollowUpActionRepository repository;

    public FollowUpActionAdapter(FollowUpActionRepository repository) {
        this.repository = repository;
    }

    public FollowUpAction save(FollowUpAction action) {
        FollowUpActionEntity entity = toEntity(action);
        FollowUpActionEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public List<FollowUpAction> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .map(this::toDomain)
                .toList();
    }

    private FollowUpActionEntity toEntity(FollowUpAction action) {
        FollowUpActionEntity entity = new FollowUpActionEntity();
        entity.setId(action.getId());
        entity.setQaReviewCaseId(action.getQaReviewCaseId());
        entity.setType(action.getType());
        entity.setDescription(action.getDescription());
        entity.setAssignedTo(action.getAssignedTo());
        entity.setStatus(action.getStatus());
        entity.setDeterminedBy(action.getDeterminedBy());
        entity.setDeterminedAt(action.getDeterminedAt());
        return entity;
    }

    private FollowUpAction toDomain(FollowUpActionEntity entity) {
        FollowUpAction action = new FollowUpAction();
        action.setId(entity.getId());
        action.setQaReviewCaseId(entity.getQaReviewCaseId());
        action.setType(entity.getType());
        action.setDescription(entity.getDescription());
        action.setAssignedTo(entity.getAssignedTo());
        action.setStatus(entity.getStatus());
        action.setDeterminedBy(entity.getDeterminedBy());
        action.setDeterminedAt(entity.getDeterminedAt());
        action.setCreatedAt(entity.getCreatedAt());
        action.setUpdatedAt(entity.getUpdatedAt());
        return action;
    }
}
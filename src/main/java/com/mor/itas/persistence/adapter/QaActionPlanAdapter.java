package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.QaActionPlan;
import com.mor.itas.persistence.jpa.entity.QaActionPlanEntity;
import com.mor.itas.persistence.jpa.repository.QaActionPlanRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaActionPlanAdapter {
    private final QaActionPlanRepository repository;

    public QaActionPlanAdapter(QaActionPlanRepository repository) {
        this.repository = repository;
    }

    public QaActionPlan save(QaActionPlan plan) {
        QaActionPlanEntity entity = toEntity(plan);
        QaActionPlanEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<QaActionPlan> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public List<QaActionPlan> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .map(this::toDomain)
                .toList();
    }

    private QaActionPlanEntity toEntity(QaActionPlan plan) {
        QaActionPlanEntity entity = new QaActionPlanEntity();
        entity.setId(plan.getId());
        entity.setQaReviewCaseId(plan.getQaReviewCaseId());
        entity.setPlanDetails(plan.getPlanDetails());
        entity.setPreparedBy(plan.getPreparedBy());
        entity.setApprovalRequestId(plan.getApprovalRequestId());
        entity.setStatus(plan.getStatus());
        return entity;
    }

    private QaActionPlan toDomain(QaActionPlanEntity entity) {
        QaActionPlan plan = new QaActionPlan();
        plan.setId(entity.getId());
        plan.setQaReviewCaseId(entity.getQaReviewCaseId());
        plan.setPlanDetails(entity.getPlanDetails());
        plan.setPreparedBy(entity.getPreparedBy());
        plan.setApprovalRequestId(entity.getApprovalRequestId());
        plan.setStatus(entity.getStatus());
        plan.setCreatedAt(entity.getCreatedAt());
        plan.setUpdatedAt(entity.getUpdatedAt());
        return plan;
    }
}
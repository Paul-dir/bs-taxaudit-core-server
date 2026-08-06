package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.jpa.entity.QaReviewCaseEntity;
import com.mor.itas.persistence.jpa.repository.QaReviewCaseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaReviewCaseAdapter {
    private final QaReviewCaseRepository repository;

    public QaReviewCaseAdapter(QaReviewCaseRepository repository) {
        this.repository = repository;
    }

    public QaReviewCase save(QaReviewCase qaReviewCase) {
        QaReviewCaseEntity entity = toEntity(qaReviewCase);
        QaReviewCaseEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<QaReviewCase> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public Optional<QaReviewCase> findByQaCaseNumber(String qaCaseNumber) {
        return repository.findAll().stream()
                .filter(e -> e.getQaCaseNumber().equals(qaCaseNumber))
                .findFirst()
                .map(this::toDomain);
    }

    public List<QaReviewCase> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private QaReviewCaseEntity toEntity(QaReviewCase qaReviewCase) {
        QaReviewCaseEntity entity = new QaReviewCaseEntity();
        entity.setId(qaReviewCase.getId());
        entity.setQaCaseNumber(qaReviewCase.getQaCaseNumber());
        entity.setOriginalCaseNumber(qaReviewCase.getOriginalCaseNumber());
        entity.setStatus(qaReviewCase.getStatus());
        entity.setSelectedAt(qaReviewCase.getSelectedAt());
        entity.setSelectionBasis(qaReviewCase.getSelectionBasis());
        entity.setAssignedReviewerId(qaReviewCase.getAssignedReviewerId());
        entity.setAssignedTeamLeadId(qaReviewCase.getAssignedTeamLeadId());
        return entity;
    }

    private QaReviewCase toDomain(QaReviewCaseEntity entity) {
        QaReviewCase qaReviewCase = new QaReviewCase();
        qaReviewCase.setId(entity.getId());
        qaReviewCase.setQaCaseNumber(entity.getQaCaseNumber());
        qaReviewCase.setOriginalCaseNumber(entity.getOriginalCaseNumber());
        qaReviewCase.setStatus(entity.getStatus());
        qaReviewCase.setSelectedAt(entity.getSelectedAt());
        qaReviewCase.setSelectionBasis(entity.getSelectionBasis());
        qaReviewCase.setAssignedReviewerId(entity.getAssignedReviewerId());
        qaReviewCase.setAssignedTeamLeadId(entity.getAssignedTeamLeadId());
        qaReviewCase.setCreatedAt(entity.getCreatedAt());
        qaReviewCase.setUpdatedAt(entity.getUpdatedAt());
        return qaReviewCase;
    }
}
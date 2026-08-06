package com.mor.itas.persistence.adapter.qa;

import com.mor.itas.domain.model.QaFinding;
import com.mor.itas.persistence.jpa.entity.QaFindingEntity;
import com.mor.itas.persistence.jpa.repository.QaFindingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaFindingAdapter {
    private final QaFindingRepository repository;

    public QaFindingAdapter(QaFindingRepository repository) {
        this.repository = repository;
    }

    public QaFinding save(QaFinding finding) {
        QaFindingEntity entity = toEntity(finding);
        QaFindingEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public List<QaFinding> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .map(this::toDomain)
                .toList();
    }

    private QaFindingEntity toEntity(QaFinding finding) {
        QaFindingEntity entity = new QaFindingEntity();
        entity.setId(finding.getId());
        entity.setQaReviewCaseId(finding.getQaReviewCaseId());
        entity.setAreaCriterionReviewed(finding.getAreaCriterionReviewed());
        entity.setOutcome(finding.getOutcome());
        entity.setEvidenceNotes(finding.getEvidenceNotes());
        entity.setRecordedBy(finding.getRecordedBy());
        entity.setCreatedAt(finding.getCreatedAt());
        return entity;
    }

    private QaFinding toDomain(QaFindingEntity entity) {
        QaFinding finding = new QaFinding();
        finding.setId(entity.getId());
        finding.setQaReviewCaseId(entity.getQaReviewCaseId());
        finding.setAreaCriterionReviewed(entity.getAreaCriterionReviewed());
        finding.setOutcome(entity.getOutcome());
        finding.setEvidenceNotes(entity.getEvidenceNotes());
        finding.setRecordedBy(entity.getRecordedBy());
        finding.setCreatedAt(entity.getCreatedAt());
        return finding;
    }
}
package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.QaReport;
import com.mor.itas.persistence.jpa.entity.QaReportEntity;
import com.mor.itas.persistence.jpa.repository.QaReportRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QaReportAdapter {
    private final QaReportRepository repository;

    public QaReportAdapter(QaReportRepository repository) {
        this.repository = repository;
    }

    public QaReport save(QaReport report) {
        QaReportEntity entity = toEntity(report);
        QaReportEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<QaReport> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    public List<QaReport> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .map(this::toDomain)
                .toList();
    }

    private QaReportEntity toEntity(QaReport report) {
        QaReportEntity entity = new QaReportEntity();
        entity.setId(report.getId());
        entity.setQaReviewCaseId(report.getQaReviewCaseId());
        entity.setVersion(report.getVersion());
        entity.setSummary(report.getSummary());
        entity.setRecommendations(report.getRecommendations());
        entity.setApprovalRequestId(report.getApprovalRequestId());
        entity.setGeneratedAt(report.getGeneratedAt());
        entity.setAdjustedAt(report.getAdjustedAt());
        return entity;
    }

    private QaReport toDomain(QaReportEntity entity) {
        QaReport report = new QaReport();
        report.setId(entity.getId());
        report.setQaReviewCaseId(entity.getQaReviewCaseId());
        report.setVersion(entity.getVersion());
        report.setSummary(entity.getSummary());
        report.setRecommendations(entity.getRecommendations());
        report.setApprovalRequestId(entity.getApprovalRequestId());
        report.setGeneratedAt(entity.getGeneratedAt());
        report.setAdjustedAt(entity.getAdjustedAt());
        return report;
    }
}
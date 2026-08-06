package com.mor.itas.application.usecase;

import com.mor.itas.domain.model.QaFinding;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.engineadapter.notification.NotificationEngineAdapter;

import java.util.UUID;

/**
 * Use case for recording a finding in a QA review.
 */
public class RecordFindingUseCase {
    private final NotificationEngineAdapter notificationAdapter;

    public RecordFindingUseCase(NotificationEngineAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public QaFinding execute(QaReviewCase reviewCase, String description, String severity) {
        // Create the finding
        QaFinding finding = QaFinding.create(reviewCase.getId(), description, severity);
        
        // Record it in the review case
        reviewCase.recordFinding(finding.getId());
        
        // Dispatch domain events
        if (reviewCase.hasDomainEvents()) {
            notificationAdapter.dispatchDomainEvents(reviewCase.pullEvents());
        }
        
        return finding;
    }
}
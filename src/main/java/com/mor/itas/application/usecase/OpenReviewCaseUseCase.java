package com.mor.itas.application.usecase;

import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.domain.valueobject.SamplingMethod;
import com.mor.itas.engineadapter.notification.NotificationEngineAdapter;

import java.util.UUID;

/**
 * Use case for opening a new QA review case.
 */
public class OpenReviewCaseUseCase {
    private final NotificationEngineAdapter notificationAdapter;

    public OpenReviewCaseUseCase(NotificationEngineAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public QaReviewCase execute(UUID caseId, UUID assignedTo, SamplingMethod samplingMethod) {
        QaReviewCase reviewCase = QaReviewCase.open(caseId, assignedTo, samplingMethod);
        
        // Dispatch domain events
        if (reviewCase.hasDomainEvents()) {
            notificationAdapter.dispatchDomainEvents(reviewCase.pullEvents());
        }
        
        return reviewCase;
    }
}
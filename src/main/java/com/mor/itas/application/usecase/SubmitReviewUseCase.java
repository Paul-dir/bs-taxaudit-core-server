package com.mor.itas.application.usecase;

import com.mor.itas.domain.exception.DomainException;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.engineadapter.notification.NotificationEngineAdapter;

import java.util.UUID;

/**
 * Use case for submitting a QA review for processing.
 */
public class SubmitReviewUseCase {
    private final NotificationEngineAdapter notificationAdapter;

    public SubmitReviewUseCase(NotificationEngineAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public void execute(QaReviewCase reviewCase, String submittedBy) {
        reviewCase.submitReview(submittedBy);
        
        // Dispatch domain events
        if (reviewCase.hasDomainEvents()) {
            notificationAdapter.dispatchDomainEvents(reviewCase.pullEvents());
        }
    }
}
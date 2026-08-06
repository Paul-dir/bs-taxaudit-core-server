package com.mor.itas.application.usecase;

import com.mor.itas.domain.model.QaReport;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.engineadapter.notification.NotificationEngineAdapter;

import java.util.UUID;

/**
 * Use case for generating a QA report.
 */
public class GenerateReportUseCase {
    private final NotificationEngineAdapter notificationAdapter;

    public GenerateReportUseCase(NotificationEngineAdapter notificationAdapter) {
        this.notificationAdapter = notificationAdapter;
    }

    public QaReport execute(QaReviewCase reviewCase, String title, String content, String summary) {
        // Generate the report
        QaReport report = QaReport.generate(reviewCase.getId(), title, content, summary);
        
        // Link it to the review case
        reviewCase.generateReport(report.getId());
        
        // Dispatch domain events
        if (reviewCase.hasDomainEvents()) {
            notificationAdapter.dispatchDomainEvents(reviewCase.pullEvents());
        }
        
        return report;
    }
}
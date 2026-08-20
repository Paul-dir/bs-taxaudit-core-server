package com.mor.itas.api.controller;

import com.mor.itas.application.usecase.GenerateReportUseCase;
import com.mor.itas.application.usecase.OpenReviewCaseUseCase;
import com.mor.itas.application.usecase.RecordFindingUseCase;
import com.mor.itas.application.usecase.SubmitReviewUseCase;
import com.mor.itas.domain.model.QaFinding;
import com.mor.itas.domain.model.QaReport;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.domain.valueobject.SamplingMethod;
import com.mor.itas.engineadapter.notification.NotificationEngineMockAdapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for QA Review Case operations.
 */
@RestController
@RequestMapping("/api/qa/review-cases")
@Tag(name = "QA Review Case", description = "QA Review Case management APIs")
public class QaReviewCaseController {

    private final OpenReviewCaseUseCase openReviewCaseUseCase;
    private final SubmitReviewUseCase submitReviewUseCase;
    private final RecordFindingUseCase recordFindingUseCase;
    private final GenerateReportUseCase generateReportUseCase;

    public QaReviewCaseController(NotificationEngineMockAdapter notificationAdapter) {
        this.openReviewCaseUseCase = new OpenReviewCaseUseCase(notificationAdapter);
        this.submitReviewUseCase = new SubmitReviewUseCase(notificationAdapter);
        this.recordFindingUseCase = new RecordFindingUseCase(notificationAdapter);
        this.generateReportUseCase = new GenerateReportUseCase(notificationAdapter);
    }

    @PostMapping
    @Operation(summary = "Open a new QA review case", description = "Creates a new QA review case for a sampled case")
    public ResponseEntity<QaReviewCase> openReviewCase(
            @Parameter(description = "Case ID to review") @RequestParam UUID caseId,
            @Parameter(description = "Assigned reviewer ID") @RequestParam UUID assignedTo,
            @Parameter(description = "Sampling method used") @RequestParam SamplingMethod samplingMethod) {
        
        QaReviewCase reviewCase = openReviewCaseUseCase.execute(caseId, assignedTo, samplingMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewCase);
    }

    @PostMapping("/{reviewCaseId}/submit")
    @Operation(summary = "Submit review for processing", description = "Submits the review case for QA review process")
    public ResponseEntity<QaReviewCase> submitReview(
            @Parameter(description = "Review case ID") @PathVariable UUID reviewCaseId,
            @Parameter(description = "User submitting the review") @RequestParam String submittedBy) {
        
        // In a real implementation, fetch from repository
        QaReviewCase reviewCase = null; // TODO: Fetch from repository
        submitReviewUseCase.execute(reviewCase, submittedBy);
        return ResponseEntity.ok(reviewCase);
    }

    @PostMapping("/{reviewCaseId}/findings")
    @Operation(summary = "Record a finding", description = "Records a new finding in the review case")
    public ResponseEntity<QaFinding> recordFinding(
            @Parameter(description = "Review case ID") @PathVariable UUID reviewCaseId,
            @Parameter(description = "Finding description") @RequestParam String description,
            @Parameter(description = "Finding severity") @RequestParam String severity) {
        
        // In a real implementation, fetch from repository
        QaReviewCase reviewCase = null; // TODO: Fetch from repository
        QaFinding finding = recordFindingUseCase.execute(reviewCase, description, severity);
        return ResponseEntity.status(HttpStatus.CREATED).body(finding);
    }

    @PostMapping("/{reviewCaseId}/report")
    @Operation(summary = "Generate QA report", description = "Generates a QA report from review findings")
    public ResponseEntity<QaReport> generateReport(
            @Parameter(description = "Review case ID") @PathVariable UUID reviewCaseId,
            @Parameter(description = "Report title") @RequestParam String title,
            @Parameter(description = "Report content") @RequestParam String content,
            @Parameter(description = "Report summary") @RequestParam String summary) {
        
        // In a real implementation, fetch from repository
        QaReviewCase reviewCase = null; // TODO: Fetch from repository
        QaReport report = generateReportUseCase.execute(reviewCase, title, content, summary);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }
}
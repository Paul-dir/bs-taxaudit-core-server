package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.GenerateReportRequest;
import com.mor.itas.domain.model.QaReport;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.QaReportAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/qa/reports")
public class QaReportController {

    private final QaReportAdapter reportAdapter;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaReportController(QaReportAdapter reportAdapter,
                              QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.reportAdapter = reportAdapter;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping("/generate")
    public ResponseEntity<QaReport> generateReport(@Valid @RequestBody GenerateReportRequest request) {
        QaReport report = new QaReport();
        report.setQaReviewCaseId(request.getQaReviewCaseId());
        report.setVersion("v1.0");
        report.setSummary(request.getSummary());
        report.setRecommendations(request.getRecommendations());
        report.setGeneratedAt(LocalDateTime.now());

        // Update QA case status
        qaReviewCaseAdapter.findById(request.getQaReviewCaseId()).ifPresent(qaCase -> {
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.DRAFT_REPORT_GENERATED);
            qaReviewCaseAdapter.save(qaCase);
        });

        QaReport saved = reportAdapter.save(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/case/{qaReviewCaseId}")
    public ResponseEntity<List<QaReport>> getReportsByCase(@PathVariable Long qaReviewCaseId) {
        return ResponseEntity.ok(reportAdapter.findByQaReviewCaseId(qaReviewCaseId));
    }

    @PostMapping("/finalize/{reportId}")
    public ResponseEntity<String> finalizeReport(@PathVariable Long reportId) {
        return reportAdapter.findById(reportId)
                .map(report -> {
                    // Update QA case status
                    qaReviewCaseAdapter.findById(report.getQaReviewCaseId()).ifPresent(qaCase -> {
                        qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.REPORT_FINALIZED);
                        qaReviewCaseAdapter.save(qaCase);
                    });
                    return ResponseEntity.ok("Report " + reportId + " finalized");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/adjust/{reportId}")
    public ResponseEntity<String> adjustReport(@PathVariable Long reportId,
                                               @RequestParam String adjustments) {
        return reportAdapter.findById(reportId)
                .map(report -> {
                    report.setSummary(report.getSummary() + " [ADJUSTED: " + adjustments + "]");
                    report.setVersion("v2.0");
                    report.setAdjustedAt(LocalDateTime.now());
                    reportAdapter.save(report);

                    // Update QA case status
                    qaReviewCaseAdapter.findById(report.getQaReviewCaseId()).ifPresent(qaCase -> {
                        qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.REPORT_ADJUSTED);
                        qaReviewCaseAdapter.save(qaCase);
                    });

                    return ResponseEntity.ok("Report " + reportId + " adjusted");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
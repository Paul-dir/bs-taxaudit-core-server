package com.mor.itas.api.controller;

import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/qa/dashboard")
public class QaDashboardController {

    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaDashboardController(QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        List<QaReviewCase> allCases = qaReviewCaseAdapter.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCases", allCases.size());

        Map<String, Long> statusCounts = allCases.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        c -> c.getStatus() != null ? c.getStatus().name() : "UNKNOWN",
                        java.util.stream.Collectors.counting()));
        stats.put("statusBreakdown", statusCounts);

        Map<String, Long> reviewerCounts = allCases.stream()
                .filter(c -> c.getAssignedReviewerId() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        QaReviewCase::getAssignedReviewerId,
                        java.util.stream.Collectors.counting()));
        stats.put("casesByReviewer", reviewerCounts);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/cases")
    public ResponseEntity<List<QaReviewCase>> getAllCases() {
        return ResponseEntity.ok(qaReviewCaseAdapter.findAll());
    }

    @GetMapping("/cases/status/{status}")
    public ResponseEntity<List<QaReviewCase>> getCasesByStatus(@PathVariable String status) {
        List<QaReviewCase> cases = qaReviewCaseAdapter.findAll().stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals(status))
                .toList();
        return ResponseEntity.ok(cases);
    }
}</arg_value><arg_key>task_progress</arg_key><arg_value>- [x] Phase QA-0: Module Bootstrap
- [x] Phase QA-1: Sampling Configuration & Periodic Selection
- [x] Phase QA-2: Auto-Assignment (FR-04.9.2-02)
- [x] Phase QA-3: Review Execution
- [x] Phase QA-4: Findings & Action Plans
- [x] Phase QA-5: Report Generation
- [x] Phase QA-6: Exit Conference
- [x] Phase QA-7: Follow-Up Actions
- [x] Phase QA-8: Closure & Notifications
- [ ] Phase QA-9: Dashboard & Analytics</arg_value></tool_call>
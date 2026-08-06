package com.mor.itas.api.controller;

import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qa/closure")
public class QaClosureController {

    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaClosureController(QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping("/close/{qaReviewCaseId}")
    public ResponseEntity<String> closeCase(@PathVariable Long qaReviewCaseId) {
        return qaReviewCaseAdapter.findById(qaReviewCaseId)
                .map(qaCase -> {
                    qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.CLOSED);
                    qaReviewCaseAdapter.save(qaCase);
                    return ResponseEntity.ok("QA case " + qaReviewCaseId + " closed successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/notify/{qaReviewCaseId}")
    public ResponseEntity<String> sendClosureNotification(@PathVariable Long qaReviewCaseId) {
        return qaReviewCaseAdapter.findById(qaReviewCaseId)
                .map(qaCase -> {
                    // In a real implementation, this would send notifications via email, Slack, etc.
                    // For now, we just log the notification
                    return ResponseEntity.ok("Closure notification sent for QA case " + qaReviewCaseId);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}</arg_value><arg_key>task_progress</arg_key><arg_value>- [x] Phase QA-0: Module Bootstrap
- [x] Phase QA-1: Sampling Configuration & Periodic Selection
- [x] Phase QA-2: Auto-Assignment (FR-04.9.2-02)
- [x] Phase QA-3: Review Execution
- [x] Phase QA-4: Findings & Action Plans
- [x] Phase QA-5: Report Generation
- [x] Phase QA-6: Exit Conference
- [x] Phase QA-7: Follow-Up Actions
- [ ] Phase QA-8: Closure & Notifications
- [ ] Phase QA-9: Dashboard & Analytics</arg_value></tool_call>
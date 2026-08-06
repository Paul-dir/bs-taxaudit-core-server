package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.SubmitReviewRequest;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qa/review")
public class QaReviewController {

    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaReviewController(QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @GetMapping("/my-cases/{reviewerId}")
    public ResponseEntity<List<QaReviewCase>> getMyAssignedCases(@PathVariable String reviewerId) {
        List<QaReviewCase> cases = qaReviewCaseAdapter.findAll().stream()
                .filter(c -> reviewerId.equals(c.getAssignedReviewerId()))
                .filter(c -> c.getStatus() == QaReviewCase.QaReviewCaseStatus.ASSIGNED)
                .toList();
        return ResponseEntity.ok(cases);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitReview(@Valid @RequestBody SubmitReviewRequest request) {
        return qaReviewCaseAdapter.findById(request.getQaReviewCaseId())
                .map(qaCase -> {
                    if (!request.getReviewerId().equals(qaCase.getAssignedReviewerId())) {
                        return ResponseEntity.badRequest()
                                .body("Reviewer " + request.getReviewerId() + " is not assigned to this case");
                    }
                    qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.REVIEW_SUBMITTED);
                    qaReviewCaseAdapter.save(qaCase);
                    return ResponseEntity.ok("Review submitted for case " + request.getQaReviewCaseId());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/approve/{qaCaseId}")
    public ResponseEntity<String> approveReview(@PathVariable Long qaCaseId) {
        return qaReviewCaseAdapter.findById(qaCaseId)
                .map(qaCase -> {
                    qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.REVIEW_APPROVED);
                    qaReviewCaseAdapter.save(qaCase);
                    return ResponseEntity.ok("Review approved for case " + qaCaseId);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
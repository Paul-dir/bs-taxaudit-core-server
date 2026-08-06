package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.CreateFindingRequest;
import com.mor.itas.domain.model.QaFinding;
import com.mor.itas.persistence.adapter.QaFindingAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qa/findings")
public class QaFindingController {

    private final QaFindingAdapter findingAdapter;

    public QaFindingController(QaFindingAdapter findingAdapter) {
        this.findingAdapter = findingAdapter;
    }

    @PostMapping
    public ResponseEntity<QaFinding> createFinding(@Valid @RequestBody CreateFindingRequest request) {
        QaFinding finding = new QaFinding();
        finding.setQaReviewCaseId(request.getQaReviewCaseId());
        finding.setAreaCriterionReviewed(request.getAreaCriterionReviewed());
        finding.setOutcome(request.getOutcome());
        finding.setEvidenceNotes(request.getEvidenceNotes());
        finding.setRecordedBy(request.getRecordedBy());

        QaFinding saved = findingAdapter.save(finding);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/case/{qaReviewCaseId}")
    public ResponseEntity<List<QaFinding>> getFindingsByCase(@PathVariable Long qaReviewCaseId) {
        return ResponseEntity.ok(findingAdapter.findByQaReviewCaseId(qaReviewCaseId));
    }
}
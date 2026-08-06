package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.CreateAssignmentRuleRequest;
import com.mor.itas.api.dto.response.AssignmentRuleResponse;
import com.mor.itas.domain.model.QaAssignmentRule;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.domain.service.QaAssignmentMatcher;
import com.mor.itas.persistence.adapter.QaAssignmentRuleAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/qa/assignment")
public class QaAssignmentController {

    private final QaAssignmentRuleAdapter assignmentRuleAdapter;
    private final QaAssignmentMatcher assignmentMatcher;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaAssignmentController(QaAssignmentRuleAdapter assignmentRuleAdapter,
                                  QaAssignmentMatcher assignmentMatcher,
                                  QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.assignmentRuleAdapter = assignmentRuleAdapter;
        this.assignmentMatcher = assignmentMatcher;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping("/rules")
    public ResponseEntity<AssignmentRuleResponse> createAssignmentRule(
            @Valid @RequestBody CreateAssignmentRuleRequest request) {
        QaAssignmentRule rule = new QaAssignmentRule();
        rule.setRuleName(request.getRuleName());
        rule.setCriteria(request.getCriteria());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());

        QaAssignmentRule saved = assignmentRuleAdapter.save(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<AssignmentRuleResponse>> getAllAssignmentRules() {
        List<AssignmentRuleResponse> rules = assignmentRuleAdapter.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rules);
    }

    @PostMapping("/auto-assign")
    public ResponseEntity<String> autoAssignReviewers() {
        List<QaReviewCase> selectedCases = qaReviewCaseAdapter.findAll().stream()
                .filter(c -> c.getStatus() == QaReviewCase.QaReviewCaseStatus.SELECTED)
                .toList();

        List<QaAssignmentRule> rules = assignmentRuleAdapter.findActiveRules();

        int assignedCount = 0;
        for (QaReviewCase qaCase : selectedCases) {
            String reviewer = assignmentMatcher.findBestReviewer(qaCase, rules);
            if (reviewer != null) {
                qaCase.setAssignedReviewerId(reviewer);
                qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.ASSIGNED);
                qaReviewCaseAdapter.save(qaCase);
                assignedCount++;
            }
        }

        return ResponseEntity.ok("Auto-assignment completed. Assigned " + assignedCount + " cases.");
    }

    @PostMapping("/reassign/{qaCaseId}")
    public ResponseEntity<String> reassignCase(@PathVariable Long qaCaseId,
                                               @RequestParam String newReviewerId) {
        return qaReviewCaseAdapter.findById(qaCaseId)
                .map(qaCase -> {
                    qaCase.setAssignedReviewerId(newReviewerId);
                    qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.ASSIGNED);
                    qaReviewCaseAdapter.save(qaCase);
                    return ResponseEntity.ok("Case " + qaCaseId + " reassigned to " + newReviewerId);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private AssignmentRuleResponse toResponse(QaAssignmentRule rule) {
        AssignmentRuleResponse response = new AssignmentRuleResponse();
        response.setId(rule.getId());
        response.setRuleName(rule.getRuleName());
        response.setCriteria(rule.getCriteria());
        response.setPriority(rule.getPriority());
        response.setActive(rule.isActive());
        response.setCreatedAt(rule.getCreatedAt());
        response.setUpdatedAt(rule.getUpdatedAt());
        return response;
    }
}
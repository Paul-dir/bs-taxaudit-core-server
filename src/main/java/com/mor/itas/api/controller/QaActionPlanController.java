package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.CreateActionPlanRequest;
import com.mor.itas.domain.model.QaActionPlan;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.QaActionPlanAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qa/action-plans")
public class QaActionPlanController {

    private final QaActionPlanAdapter actionPlanAdapter;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaActionPlanController(QaActionPlanAdapter actionPlanAdapter,
                                  QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.actionPlanAdapter = actionPlanAdapter;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping
    public ResponseEntity<QaActionPlan> createActionPlan(@Valid @RequestBody CreateActionPlanRequest request) {
        QaActionPlan plan = new QaActionPlan();
        plan.setQaReviewCaseId(request.getQaReviewCaseId());
        plan.setPlanDetails(request.getPlanDetails());
        plan.setPreparedBy(request.getPreparedBy());
        plan.setStatus("SUBMITTED");

        // Update QA case status
        qaReviewCaseAdapter.findById(request.getQaReviewCaseId()).ifPresent(qaCase -> {
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.PLAN_SUBMITTED);
            qaReviewCaseAdapter.save(qaCase);
        });

        QaActionPlan saved = actionPlanAdapter.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/approve/{actionPlanId}")
    public ResponseEntity<String> approveActionPlan(@PathVariable Long actionPlanId) {
        return actionPlanAdapter.findById(actionPlanId)
                .map(plan -> {
                    plan.setStatus("APPROVED");
                    actionPlanAdapter.save(plan);

                    // Update QA case status
                    qaReviewCaseAdapter.findById(plan.getQaReviewCaseId()).ifPresent(qaCase -> {
                        qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.PLAN_APPROVED);
                        qaReviewCaseAdapter.save(qaCase);
                    });

                    return ResponseEntity.ok("Action plan " + actionPlanId + " approved");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.CreateFollowUpActionRequest;
import com.mor.itas.domain.model.FollowUpAction;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.persistence.adapter.FollowUpActionAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/qa/follow-up")
public class FollowUpActionController {

    private final FollowUpActionAdapter followUpActionAdapter;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public FollowUpActionController(FollowUpActionAdapter followUpActionAdapter,
                                    QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.followUpActionAdapter = followUpActionAdapter;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping("/actions")
    public ResponseEntity<FollowUpAction> createFollowUpAction(@Valid @RequestBody CreateFollowUpActionRequest request) {
        FollowUpAction action = new FollowUpAction();
        action.setQaReviewCaseId(request.getQaReviewCaseId());
        action.setType(request.getType());
        action.setDescription(request.getDescription());
        action.setAssignedTo(request.getAssignedTo());
        action.setStatus("PENDING");
        action.setDeterminedBy("SYSTEM");
        action.setDeterminedAt(LocalDateTime.now());

        qaReviewCaseAdapter.findById(request.getQaReviewCaseId()).ifPresent(qaCase -> {
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.FOLLOW_UP_DETERMINED);
            qaReviewCaseAdapter.save(qaCase);
        });

        FollowUpAction saved = followUpActionAdapter.save(action);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/actions/case/{qaReviewCaseId}")
    public ResponseEntity<List<FollowUpAction>> getFollowUpActionsByCase(@PathVariable Long qaReviewCaseId) {
        return ResponseEntity.ok(followUpActionAdapter.findByQaReviewCaseId(qaReviewCaseId));
    }

    @PostMapping("/actions/complete/{actionId}")
    public ResponseEntity<String> completeFollowUpAction(@PathVariable Long actionId) {
        return followUpActionAdapter.findByQaReviewCaseId(actionId).stream()
                .findFirst()
                .map(action -> {
                    action.setStatus("COMPLETED");
                    followUpActionAdapter.save(action);
                    return ResponseEntity.ok("Follow-up action " + actionId + " completed");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}</arg_value>
<task_progress>
- [x] Phase QA-0: Module Bootstrap
- [x] Phase QA-1: Sampling Configuration & Periodic Selection
- [x] Phase QA-2: Auto-Assignment (FR-04.9.2-02)
- [x] Phase QA-3: Review Execution
- [x] Phase QA-4: Findings & Action Plans
- [x] Phase QA-5: Report Generation
- [x] Phase QA-6: Exit Conference
- [ ] Phase QA-7: Follow-Up Actions
- [ ] Phase QA-8: Closure & Notifications
- [ ] Phase QA-9: Dashboard & Analytics
</task_progress></tool_call>
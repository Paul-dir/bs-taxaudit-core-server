package com.mor.itas.planning.api.controller.backoffice;

import com.itas.bs.taxaudit.api.dto.request.IntakeAuditReferralRequest;
import com.itas.bs.taxaudit.api.dto.request.ReviewAuditReferralRequest;
import com.itas.bs.taxaudit.api.dto.response.AuditReferralListResponse;
import com.itas.bs.taxaudit.api.dto.response.AuditReferralResponse;
import com.itas.bs.taxaudit.application.usecase.GetAuditReferralUseCase;
import com.itas.bs.taxaudit.application.usecase.GetPendingReferralsUseCase;
import com.itas.bs.taxaudit.application.usecase.IntakeAuditReferralUseCase;
import com.itas.bs.taxaudit.application.usecase.ReviewAuditReferralUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/backoffice/audit-referrals")
@RequiredArgsConstructor
public class AuditReferralController {

    private final IntakeAuditReferralUseCase intakeUseCase;
    private final ReviewAuditReferralUseCase reviewUseCase;
    private final GetAuditReferralUseCase getUseCase;
    private final GetPendingReferralsUseCase getPendingUseCase;

    @PostMapping
    public ResponseEntity<UUID> intakeReferral(@RequestBody IntakeAuditReferralRequest request) {
        UUID referralId = intakeUseCase.execute(
                request.getSource(),
                request.getReferringEntity(),
                request.getReferenceDetails(),
                request.getRelatedTaxpayerTin()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(referralId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditReferralResponse> getReferral(@PathVariable UUID id) {
        return ResponseEntity.ok(new AuditReferralResponse(getUseCase.getById(id)));
    }

    @GetMapping
    public ResponseEntity<AuditReferralListResponse> getAllReferrals() {
        return ResponseEntity.ok(new AuditReferralListResponse(getUseCase.getAll()));
    }

    @GetMapping("/pending")
    public ResponseEntity<AuditReferralListResponse> getPendingReferrals() {
        return ResponseEntity.ok(new AuditReferralListResponse(getPendingUseCase.getPending()));
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Void> reviewReferral(@PathVariable UUID id, @RequestBody ReviewAuditReferralRequest request) {
        reviewUseCase.execute(id, request.isAccept(), request.getActorId(), request.getReason(), request.getAnnualPlanId());
        return ResponseEntity.ok().build();
    }
}

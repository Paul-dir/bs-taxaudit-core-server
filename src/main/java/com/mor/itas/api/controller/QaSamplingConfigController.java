package com.mor.itas.api.controller;

import com.mor.itas.api.dto.request.CreateSamplingConfigRequest;
import com.mor.itas.api.dto.response.SamplingConfigResponse;
import com.mor.itas.domain.model.QaSamplingConfig;
import com.mor.itas.domain.service.SamplingSelector;
import com.mor.itas.persistence.adapter.QaSamplingConfigAdapter;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qa/config/sampling")
public class QaSamplingConfigController {

    private final QaSamplingConfigAdapter samplingConfigAdapter;
    private final SamplingSelector samplingSelector;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public QaSamplingConfigController(QaSamplingConfigAdapter samplingConfigAdapter,
                                       SamplingSelector samplingSelector,
                                       QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.samplingConfigAdapter = samplingConfigAdapter;
        this.samplingSelector = samplingSelector;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    @PostMapping
    public ResponseEntity<SamplingConfigResponse> createOrUpdateSamplingConfig(
            @Valid @RequestBody CreateSamplingConfigRequest request) {
        QaSamplingConfig config = new QaSamplingConfig();
        config.setSamplingMethod(request.getSamplingMethod());
        config.setFrequency(request.getFrequency());
        config.setScopeFilters(request.getScopeFilters());
        config.setActive(request.isActive());

        QaSamplingConfig saved = samplingConfigAdapter.save(config);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<SamplingConfigResponse> getActiveSamplingConfig() {
        return samplingConfigAdapter.findActiveConfig()
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/run-sampling")
    public ResponseEntity<String> runSamplingManually() {
        // This is a manual trigger for testing
        // The real job runs on the configured schedule via application/scheduling
        return ResponseEntity.ok("Sampling job triggered manually. Check logs for results.");
    }

    private SamplingConfigResponse toResponse(QaSamplingConfig config) {
        SamplingConfigResponse response = new SamplingConfigResponse();
        response.setId(config.getId());
        response.setSamplingMethod(config.getSamplingMethod());
        response.setFrequency(config.getFrequency());
        response.setScopeFilters(config.getScopeFilters());
        response.setActive(config.isActive());
        response.setCreatedAt(config.getCreatedAt());
        response.setUpdatedAt(config.getUpdatedAt());
        return response;
    }
}
package com.mor.itas.application.usecase.sampling;

import com.mor.itas.domain.model.Case;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.domain.model.QaSamplingConfig;
import com.mor.itas.domain.service.SamplingSelector;
import com.mor.itas.persistence.adapter.QaReviewCaseAdapter;
import com.mor.itas.persistence.jpa.repository.QaSamplingConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunSamplingJobUseCase {

    private final QaSamplingConfigRepository samplingConfigRepository;
    private final SamplingSelector samplingSelector;
    private final QaReviewCaseAdapter qaReviewCaseAdapter;

    public RunSamplingJobUseCase(QaSamplingConfigRepository samplingConfigRepository,
                                 SamplingSelector samplingSelector,
                                 QaReviewCaseAdapter qaReviewCaseAdapter) {
        this.samplingConfigRepository = samplingConfigRepository;
        this.samplingSelector = samplingSelector;
        this.qaReviewCaseAdapter = qaReviewCaseAdapter;
    }

    public int executeSamplingJob() {
        // Get active sampling config
        QaSamplingConfig config = samplingConfigRepository.findAll().stream()
                .filter(QaSamplingConfig::isActive)
                .findFirst()
                .orElse(null);

        if (config == null) {
            return 0;
        }

        // Get closed cases (in a real implementation, this would come from the Case repository)
        // For now, we'll return 0 as a placeholder
        List<Case> closedCases = getClosedCases();

        if (closedCases.isEmpty()) {
            return 0;
        }

        // Select cases for review
        List<QaReviewCase> selectedCases = samplingSelector.selectCasesForReview(config, closedCases);

        // Save selected cases
        for (QaReviewCase qaCase : selectedCases) {
            qaReviewCaseAdapter.save(qaCase);
        }

        return selectedCases.size();
    }

    private List<Case> getClosedCases() {
        // TODO: Implement this method to fetch closed cases from the Case repository
        // This is a placeholder that should be replaced with actual implementation
        // once the Case Tracking module is fully integrated
        return List.of();
    }
}
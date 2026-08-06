package com.mor.itas.domain.service;

import com.mor.itas.domain.model.Case;
import com.mor.itas.domain.model.QaReviewCase;
import com.mor.itas.domain.model.QaSamplingConfig;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SamplingSelector {

    public List<QaReviewCase> selectCasesForReview(QaSamplingConfig config, List<Case> closedCases) {
        if (config == null || !config.isActive()) {
            return new ArrayList<>();
        }

        return switch (config.getSamplingMethod()) {
            case RANDOM -> selectRandom(config, closedCases);
            case RISK_BASED -> selectRiskBased(config, closedCases);
            case STRATIFIED -> selectStratified(config, closedCases);
            case PERCENTAGE_BASED -> selectPercentageBased(config, closedCases);
        };
    }

    private List<QaReviewCase> selectRandom(QaSamplingConfig config, List<Case> closedCases) {
        double percentage = 0.10;
        int sampleSize = (int) Math.ceil(closedCases.size() * percentage);
        sampleSize = Math.min(sampleSize, closedCases.size());

        List<Case> selectedCases = new ArrayList<>(closedCases);
        Collections.shuffle(selectedCases);
        selectedCases = selectedCases.subList(0, sampleSize);

        return mapToQaReviewCases(selectedCases, config, "RANDOM sampling");
    }

    private List<QaReviewCase> selectRiskBased(QaSamplingConfig config, List<Case> closedCases) {
        List<Case> sortedCases = new ArrayList<>(closedCases);
        Collections.shuffle(sortedCases);

        int sampleSize = (int) Math.ceil(sortedCases.size() * 0.20);
        sampleSize = Math.min(sampleSize, sortedCases.size());
        sortedCases = sortedCases.subList(0, sampleSize);

        return mapToQaReviewCases(sortedCases, config, "RISK_BASED sampling");
    }

    private List<QaReviewCase> selectStratified(QaSamplingConfig config, List<Case> closedCases) {
        Map<Case.CaseType, List<Case>> stratified = new HashMap<>();
        for (Case caseObj : closedCases) {
            stratified.computeIfAbsent(caseObj.getType(), k -> new ArrayList<>()).add(caseObj);
        }

        List<QaReviewCase> result = new ArrayList<>();
        for (List<Case> stratum : stratified.values()) {
            int sampleSize = Math.max(1, (int) Math.ceil(stratum.size() * 0.10));
            Collections.shuffle(stratum);
            List<Case> sampled = stratum.subList(0, Math.min(sampleSize, stratum.size()));
            result.addAll(mapToQaReviewCases(sampled, config, "STRATIFIED sampling"));
        }

        return result;
    }

    private List<QaReviewCase> selectPercentageBased(QaSamplingConfig config, List<Case> closedCases) {
        double percentage = 0.10;
        if (config.getScopeFilters() != null && config.getScopeFilters().contains("percentage")) {
            percentage = 0.10;
        }

        int sampleSize = (int) Math.ceil(closedCases.size() * percentage);
        sampleSize = Math.min(sampleSize, closedCases.size());

        List<Case> selectedCases = new ArrayList<>(closedCases);
        Collections.shuffle(selectedCases);
        selectedCases = selectedCases.subList(0, sampleSize);

        return mapToQaReviewCases(selectedCases, config, "PERCENTAGE_BASED sampling");
    }

    private List<QaReviewCase> mapToQaReviewCases(List<Case> cases, QaSamplingConfig config, String basis) {
        List<QaReviewCase> qaCases = new ArrayList<>();
        for (Case caseObj : cases) {
            QaReviewCase qaCase = new QaReviewCase();
            qaCase.setOriginalCaseNumber(caseObj.getCaseNumber());
            qaCase.setStatus(QaReviewCase.QaReviewCaseStatus.SELECTED);
            qaCase.setSelectedAt(LocalDateTime.now());
            qaCase.setSelectionBasis(basis + " - config: " + config.getId());
            qaCases.add(qaCase);
        }
        return qaCases;
    }
}
package com.mor.itas.domain.service;

import com.mor.itas.domain.model.QaAssignmentRule;
import com.mor.itas.domain.model.QaReviewCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QaAssignmentMatcher {

    public String findBestReviewer(QaReviewCase qaCase, List<QaAssignmentRule> rules) {
        // Sort rules by priority (lower number = higher priority)
        List<QaAssignmentRule> sortedRules = rules.stream()
                .filter(QaAssignmentRule::isActive)
                .sorted((r1, r2) -> Integer.compare(r1.getPriority(), r2.getPriority()))
                .toList();

        // For each rule, check if the criteria matches the case
        for (QaAssignmentRule rule : sortedRules) {
            if (matchesCriteria(rule, qaCase)) {
                return rule.getRuleName();
            }
        }

        // Default: no specific rule matched, return null
        return null;
    }

    private boolean matchesCriteria(QaAssignmentRule rule, QaReviewCase qaCase) {
        String criteria = rule.getCriteria().toLowerCase();
        String caseInfo = (qaCase.getOriginalCaseNumber() + " " +
                (qaCase.getSelectionBasis() != null ? qaCase.getSelectionBasis() : "")).toLowerCase();

        // Simple keyword matching
        if (criteria.contains("high_risk") && caseInfo.contains("risk")) {
            return true;
        }
        if (criteria.contains("complex") && caseInfo.contains("complex")) {
            return true;
        }
        if (criteria.contains("all") || criteria.contains("default")) {
            return true;
        }

        return false;
    }
}
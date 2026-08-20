package com.mor.itas.domain.valueobject;

/**
 * Outcome of a QA finding assessment.
 */
public enum FindingOutcome {
    CONFORMED,      // Finding confirmed - non-conformity exists
    NOT_CONFORMED,  // Finding not confirmed
    PENDING         // Assessment pending
}
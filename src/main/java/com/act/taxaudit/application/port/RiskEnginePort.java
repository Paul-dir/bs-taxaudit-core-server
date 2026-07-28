package com.act.taxaudit.application.port;

/**
 * Port for Risk Engine operations.
 */
public interface RiskEnginePort {
    void updateRiskProfile(String tin, String justification, java.util.Map<String, Object> indicators);
}
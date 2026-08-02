package com.mor.itas.planning.application.port;

import java.math.BigDecimal;

public interface LedgerEnginePort {
    void postAssessment(String tin, String caseRef, BigDecimal amount);
}

package com.mor.itas.planning.application.port;

import lombok.Value;

public interface RegistrationServicePort {
    TaxpayerSummary lookupTaxpayer(String tin);

    @Value
    class TaxpayerSummary {
        String tin;
        String name;
        String taxpayerSegment; // LTO, MTO, STO
        String region;
        String status; // ACTIVE, INACTIVE
    }
}

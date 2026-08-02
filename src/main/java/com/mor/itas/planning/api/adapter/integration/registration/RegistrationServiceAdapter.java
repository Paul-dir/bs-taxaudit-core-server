package com.mor.itas.planning.engineadapter.integration.registration;

import com.itas.bs.taxaudit.application.port.RegistrationServicePort;
import org.springframework.stereotype.Component;

@Component
public class RegistrationServiceAdapter implements RegistrationServicePort {
    @Override
    public TaxpayerSummary lookupTaxpayer(String tin) {
        return new TaxpayerSummary(tin, "Acme Corp", "LTO", "Addis Ababa", "ACTIVE");
    }
}

package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateAnnualAuditPlanRequest {
    private int year;
    private int totalCasesPlanned;
    private UUID directorId;
}

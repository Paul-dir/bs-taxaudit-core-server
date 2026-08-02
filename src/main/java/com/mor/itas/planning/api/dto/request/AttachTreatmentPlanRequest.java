package com.mor.itas.planning.api.dto.request;

import com.itas.bs.taxaudit.domain.valueobject.ComplexityRating;
import com.itas.bs.taxaudit.domain.valueobject.TreatmentPlanType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AttachTreatmentPlanRequest {
    private TreatmentPlanType planType;
    private String recommendedActions;
    private LocalDate targetCompletionDate;
    private ComplexityRating complexityRating;
}

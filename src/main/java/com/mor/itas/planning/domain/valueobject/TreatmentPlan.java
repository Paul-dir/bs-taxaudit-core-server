package com.mor.itas.planning.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class TreatmentPlan {
    TreatmentPlanType planType;
    String recommendedActions;
    LocalDate targetCompletionDate;
    ComplexityRating complexityRating;
}

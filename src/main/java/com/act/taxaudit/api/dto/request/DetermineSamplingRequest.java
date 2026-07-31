package com.act.taxaudit.api.dto.request;

import com.act.taxaudit.domain.valueobject.SamplingMethod;
import java.util.List;

public record DetermineSamplingRequest(SamplingMethod method, String criteria,
                                       List<String> selectedItems, int sampleSize) {}
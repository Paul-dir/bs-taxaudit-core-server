package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class ReturnPlanWithCommentsRequest {
    private UUID actorId;
    private UUID workflowInstanceId;
    private String comments;
}

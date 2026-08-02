package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class SubmitPlanToDirectorRequest {
    private UUID actorId;
}

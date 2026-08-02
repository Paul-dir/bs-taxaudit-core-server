package com.mor.itas.planning.api.dto.request;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class DistributeCasesRequest {
    private List<UUID> caseIds;
    private String method; // "AUTO" or "MANUAL"
    private UUID targetUserId; // Used if method is MANUAL
}

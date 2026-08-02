package com.mor.itas.planning.domain.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class OrgContext {
    private final String assignedRegion;
    private final String assignedTaxCenter;
    private final AuditType auditType;
    private final String teamId;
}

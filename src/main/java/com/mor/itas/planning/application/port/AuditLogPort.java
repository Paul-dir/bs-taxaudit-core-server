package com.mor.itas.planning.application.port;

import java.util.UUID;

public interface AuditLogPort {
    void log(String aggregateType, UUID aggregateId, String action, UUID actorId, String reason, String diff);
}

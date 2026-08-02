package com.mor.itas.planning.application.port;

public interface IdempotencyStorePort {
    boolean exists(String idempotencyKey);
    void save(String idempotencyKey, String responsePayload);
}

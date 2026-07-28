package com.act.taxaudit.engineadapter.dms;

import com.act.taxaudit.application.port.DmsPort;
import com.act.taxaudit.engineadapter.shared.BaseEngineAdapter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class DmsMockAdapter extends BaseEngineAdapter implements DmsPort {

    @Override
    @CircuitBreaker(name = "dms", fallbackMethod = "fallbackStoreDocument")
    @Retry(name = "dms")
    public String storeDocument(String deskAuditId, byte[] content, String fileName, String mimeType) {
        logMockCall("storeDocument");
        return "dms-ref-" + deskAuditId + "-" + System.currentTimeMillis();
    }

    @Override
    @CircuitBreaker(name = "dms", fallbackMethod = "fallbackFetchDocument")
    @Retry(name = "dms")
    public byte[] fetchDocument(String documentReference) {
        logMockCall("fetchDocument");
        return new byte[0];
    }

    public String fallbackStoreDocument(String deskAuditId, byte[] content, String fileName, String mimeType, Throwable t) {
        log.error("Fallback for storeDocument({}): {}", deskAuditId, t.getMessage());
        return null;
    }

    public byte[] fallbackFetchDocument(String documentReference, Throwable t) {
        log.error("Fallback for fetchDocument({}): {}", documentReference, t.getMessage());
        return new byte[0];
    }
}
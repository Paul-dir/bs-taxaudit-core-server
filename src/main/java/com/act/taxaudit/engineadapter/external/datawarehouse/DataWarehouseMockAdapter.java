package com.act.taxaudit.engineadapter.external.datawarehouse;

import com.act.taxaudit.application.port.DataWarehousePort;
import com.act.taxaudit.engineadapter.shared.BaseEngineAdapter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataWarehouseMockAdapter extends BaseEngineAdapter implements DataWarehousePort {

    @Override
    @CircuitBreaker(name = "data-warehouse", fallbackMethod = "fallbackPullTaxpayerData")
    @Retry(name = "data-warehouse")
    public Map<String, Object> pullTaxpayerData(String tin) {
        logMockCall("pullTaxpayerData");
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("taxpayerName", "Mock Taxpayer");
        data.put("taxType", "VAT");
        return data;
    }

    @Override
    @CircuitBreaker(name = "data-warehouse", fallbackMethod = "fallbackPullHistoricalAuditData")
    @Retry(name = "data-warehouse")
    public Map<String, Object> pullHistoricalAuditData(String tin) {
        logMockCall("pullHistoricalAuditData");
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("previousAuditCount", 0);
        data.put("lastAuditDate", null);
        return data;
    }

    public Map<String, Object> fallbackPullTaxpayerData(String tin, Throwable t) {
        log.error("Fallback for pullTaxpayerData({}): {}", tin, t.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("error", "Service unavailable");
        return data;
    }

    public Map<String, Object> fallbackPullHistoricalAuditData(String tin, Throwable t) {
        log.error("Fallback for pullHistoricalAuditData({}): {}", tin, t.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("error", "Service unavailable");
        return data;
    }
}
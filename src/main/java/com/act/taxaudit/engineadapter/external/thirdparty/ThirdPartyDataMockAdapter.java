package com.act.taxaudit.engineadapter.external.thirdparty;

import com.act.taxaudit.application.port.ThirdPartyDataPort;
import com.act.taxaudit.engineadapter.shared.BaseEngineAdapter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdPartyDataMockAdapter extends BaseEngineAdapter implements ThirdPartyDataPort {

    @Override
    @CircuitBreaker(name = "third-party-data", fallbackMethod = "fallbackPullThirdPartyData")
    @Retry(name = "third-party-data")
    public Map<String, Object> pullThirdPartyData(String tin) {
        logMockCall("pullThirdPartyData");
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("source", "ThirdPartyMock");
        data.put("status", "OK");
        return data;
    }

    public Map<String, Object> fallbackPullThirdPartyData(String tin, Throwable t) {
        log.error("Fallback for pullThirdPartyData({}): {}", tin, t.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("tin", tin);
        data.put("error", "Service unavailable");
        return data;
    }
}
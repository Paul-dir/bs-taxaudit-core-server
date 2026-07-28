package com.act.taxaudit.engineadapter.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all engine adapters, providing common logging and error handling.
 */
public abstract class BaseEngineAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected void logMockCall(String methodName) {
        log.info("[MOCK] {} called - returning default response", methodName);
    }
}
package com.act.taxaudit.application.port;

import java.util.Map;

/**
 * Port for pulling data from third-party sources.
 */
public interface ThirdPartyDataPort {
    Map<String, Object> pullThirdPartyData(String tin);
}
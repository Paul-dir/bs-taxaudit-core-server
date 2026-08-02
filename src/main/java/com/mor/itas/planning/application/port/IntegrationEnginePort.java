package com.mor.itas.planning.application.port;

public interface IntegrationEnginePort {
    String fetchThirdPartyData(String tin, String sourceType);
}

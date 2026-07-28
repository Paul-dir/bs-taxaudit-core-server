package com.act.taxaudit.application.port;

import java.util.Map;

/**
 * Port for pulling internal data from the data warehouse.
 */
public interface DataWarehousePort {
    Map<String, Object> pullTaxpayerData(String tin);
    Map<String, Object> pullHistoricalAuditData(String tin);
}
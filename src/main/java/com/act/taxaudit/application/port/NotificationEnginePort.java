package com.act.taxaudit.application.port;

/**
 * Port for Notification Engine operations.
 */
public interface NotificationEnginePort {
    void sendNotification(String tin, String templateCode, java.util.Map<String, Object> variables);
}
package com.act.taxaudit.engineadapter.notification;

import com.act.taxaudit.application.port.NotificationEnginePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationEngineMockAdapter implements NotificationEnginePort {

    private static final Logger log = LoggerFactory.getLogger(NotificationEngineMockAdapter.class);

    @Override
    public void sendNotification(String tin, String templateCode, Map<String, Object> variables) {
        log.info("Mock notification sent - TIN: {}, Template: {}, Variables: {}", tin, templateCode, variables);
    }
}
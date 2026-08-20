package com.mor.itas.engineadapter.notification;

import com.mor.itas.domain.event.DomainEvent;

import java.util.List;

/**
 * Notification engine adapter for sending notifications.
 * Mock implementation for development/testing.
 */
public interface NotificationEngineAdapter {
    void sendNotification(String recipient, String subject, String body);
    void sendBulkNotification(List<String> recipients, String subject, String body);
    void dispatchDomainEvents(List<DomainEvent> events);
}
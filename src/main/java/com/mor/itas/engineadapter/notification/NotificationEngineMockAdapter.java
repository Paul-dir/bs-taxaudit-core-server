package com.mor.itas.engineadapter.notification;

import com.mor.itas.domain.event.DomainEvent;

import java.util.List;

/**
 * Mock notification engine adapter for development and testing.
 */
public class NotificationEngineMockAdapter implements NotificationEngineAdapter {

    @Override
    public void sendNotification(String recipient, String subject, String body) {
        System.out.println("[NotificationMock] To: " + recipient + ", Subject: " + subject);
        System.out.println("  Body: " + body);
    }

    @Override
    public void sendBulkNotification(List<String> recipients, String subject, String body) {
        System.out.println("[NotificationMock] Bulk notification to " + recipients.size() + " recipients");
        System.out.println("  Subject: " + subject);
        for (String recipient : recipients) {
            System.out.println("  To: " + recipient);
        }
    }

    @Override
    public void dispatchDomainEvents(List<DomainEvent> events) {
        System.out.println("[NotificationMock] Dispatching " + events.size() + " domain events");
        for (DomainEvent event : events) {
            System.out.println("  Event: " + event.getClass().getSimpleName() + " at " + event.occurredAt());
        }
    }
}
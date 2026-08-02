package com.mor.itas.planning.application.port;

import java.util.UUID;

public interface NotificationServicePort {
    void sendNotification(UUID recipientId, String templateCode, String payload);
}

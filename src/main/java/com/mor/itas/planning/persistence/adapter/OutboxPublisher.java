package com.mor.itas.planning.persistence.adapter;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OutboxPublisher {
    
    public void publishToBroker(String eventType, String aggregateId, String payload) {
        // In a real implementation, this would use KafkaTemplate or RabbitTemplate
        log.info("Publishing event {} for aggregate {}: {}", eventType, aggregateId, payload);
    }
}

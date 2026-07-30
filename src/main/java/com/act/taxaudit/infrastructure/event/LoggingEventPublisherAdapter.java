package com.act.taxaudit.infrastructure.event;

import com.act.taxaudit.application.port.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingEventPublisherAdapter.class);

    @Override
    public void publish(Object event) {
        log.info("Event published: {}", event.getClass().getSimpleName());
    }
}
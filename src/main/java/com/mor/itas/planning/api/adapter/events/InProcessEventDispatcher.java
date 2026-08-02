package com.mor.itas.planning.engineadapter.events;

import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.application.port.OutboxPort;
import com.itas.bs.taxaudit.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InProcessEventDispatcher implements EventDispatchPort {
    private final OutboxPort outboxPort;

    @Override
    public void dispatch(DomainEvent event) {
        log.info("Dispatching domain event in-process: {}", event.getEventType());
        outboxPort.save(event);
    }
}

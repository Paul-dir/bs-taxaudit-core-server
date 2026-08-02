package com.mor.itas.planning.application.port;

import com.itas.bs.taxaudit.domain.event.DomainEvent;

public interface EventDispatchPort {
    void dispatch(DomainEvent event);
}

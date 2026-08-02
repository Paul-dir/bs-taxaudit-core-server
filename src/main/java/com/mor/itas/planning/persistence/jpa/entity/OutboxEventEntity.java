package com.mor.itas.planning.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEventEntity {
    @Id
    private UUID id;
    
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    private String payload;
    private Instant createdAt;
    private boolean published;
    private Instant publishedAt;
}

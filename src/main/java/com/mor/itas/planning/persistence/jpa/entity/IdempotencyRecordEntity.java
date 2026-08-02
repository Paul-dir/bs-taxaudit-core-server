package com.mor.itas.planning.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "idempotency_record")
@Getter
@Setter
public class IdempotencyRecordEntity {
    @Id
    private String idempotencyKey;
    private String responsePayload;
    private Instant createdAt;
}

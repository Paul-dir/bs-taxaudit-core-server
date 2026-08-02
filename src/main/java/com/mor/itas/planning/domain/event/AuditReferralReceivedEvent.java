package com.mor.itas.planning.domain.event;

import com.itas.bs.taxaudit.domain.valueobject.AuditCaseSource;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class AuditReferralReceivedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant occurredAt = Instant.now();
    UUID referralId;
    AuditCaseSource sourceType;
    String referringEntity;
    String relatedTaxpayerTin;

    @Override
    public UUID getAggregateId() {
        return referralId;
    }

    @Override
    public EventType getEventType() {
        return EventType.AUDIT_REFERRAL_RECEIVED;
    }
}

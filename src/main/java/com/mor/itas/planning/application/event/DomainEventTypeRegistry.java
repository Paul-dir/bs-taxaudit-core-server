package com.mor.itas.planning.application.event;

import com.itas.bs.taxaudit.domain.event.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DomainEventTypeRegistry {
    private final Map<String, Class<? extends DomainEvent>> registry = new HashMap<>();

    public DomainEventTypeRegistry() {
        // AP Cluster
        registry.put(EventType.ANNUAL_AUDIT_PLAN_CREATED.name(), AnnualAuditPlanCreatedEvent.class);
        registry.put(EventType.ANNUAL_AUDIT_PLAN_APPROVED.name(), AnnualAuditPlanApprovedEvent.class);
        registry.put(EventType.AUDIT_CASE_CREATED.name(), AuditCaseCreatedEvent.class);
        registry.put(EventType.AUDIT_REFERRAL_RECEIVED.name(), AuditReferralReceivedEvent.class);
        registry.put(EventType.AUDIT_CASE_SELECTED.name(), AuditCaseSelectedEvent.class);
        registry.put(EventType.RANDOM_AUDIT_CASE_SELECTED.name(), RandomAuditCaseSelectedEvent.class);
        registry.put(EventType.TREATMENT_PLAN_ATTACHED.name(), TreatmentPlanAttachedEvent.class);
        registry.put(EventType.AUDIT_CASE_ASSIGNED.name(), AuditCaseAssignedEvent.class);
        registry.put(EventType.AUDIT_CASE_REASSIGNED.name(), AuditCaseReassignedEvent.class);
    }

    public Class<? extends DomainEvent> resolve(String eventType) {
        return registry.get(eventType);
    }
}

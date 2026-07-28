-- Desk Audit schema for ITAS Tax Audit Service
-- Mirrors the bs-filling pattern with JSONB for nested collections

CREATE TABLE IF NOT EXISTS desk_audits (
    id                          UUID PRIMARY KEY,
    audit_case_id               UUID          NOT NULL,
    tin                         VARCHAR(32)   NOT NULL,
    status                      VARCHAR(32)   NOT NULL,
    evidence_items_json         JSONB         NOT NULL DEFAULT '[]'::jsonb,
    document_requests_json      JSONB         NOT NULL DEFAULT '[]'::jsonb,
    sampling_method             VARCHAR(16),
    sample_selection_json       JSONB,
    findings_json               JSONB         NOT NULL DEFAULT '[]'::jsonb,
    draft_report_json           JSONB,
    team_leader_decision        VARCHAR(32),
    team_leader_actor_id        VARCHAR(128),
    team_leader_narrative       VARCHAR(2048),
    team_leader_decided_at      TIMESTAMP WITH TIME ZONE,
    escalation_decision         VARCHAR(32),
    escalated_at                TIMESTAMP WITH TIME ZONE,
    fraud_flag_json             JSONB,
    fraud_notes                 VARCHAR(2048),
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL,
    version                     BIGINT        NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS ix_desk_audits_case ON desk_audits (audit_case_id);
CREATE INDEX IF NOT EXISTS ix_desk_audits_status ON desk_audits (status);
CREATE INDEX IF NOT EXISTS ix_desk_audits_tin ON desk_audits (tin);

-- Outbox entries table (transactional outbox pattern)
CREATE TABLE IF NOT EXISTS outbox_entries (
    id              UUID PRIMARY KEY,
    aggregate_type  VARCHAR(128)  NOT NULL,
    aggregate_id    VARCHAR(64)   NOT NULL,
    event_type      VARCHAR(128)  NOT NULL,
    payload         JSONB         NOT NULL,
    status          VARCHAR(16)   NOT NULL DEFAULT 'PENDING',
    attempt_count   INTEGER       NOT NULL DEFAULT 0,
    max_attempts    INTEGER       NOT NULL DEFAULT 5,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_attempt_at TIMESTAMP WITH TIME ZONE,
    processed_at    TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS ix_outbox_status ON outbox_entries (status, created_at);
CREATE INDEX IF NOT EXISTS ix_outbox_aggregate ON outbox_entries (aggregate_type, aggregate_id);

-- Consumer idempotency table (deduplication of Kafka events)
CREATE TABLE IF NOT EXISTS processed_events (
    event_id        UUID PRIMARY KEY,
    event_type      VARCHAR(128)  NOT NULL,
    topic           VARCHAR(128)  NOT NULL,
    partition_id    INTEGER       NOT NULL,
    offset_id       BIGINT        NOT NULL,
    consumed_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS ix_processed_events_expires ON processed_events (expires_at);
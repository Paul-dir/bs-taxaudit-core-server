-- QA Review Module Schema
-- Phase QA-0: Module Bootstrap

-- QA Sampling Configuration
CREATE TABLE IF NOT EXISTS qa_sampling_configs (
    id BIGSERIAL PRIMARY KEY,
    sampling_method VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    scope_filters JSONB,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- QA Review Cases (aggregate root)
CREATE TABLE IF NOT EXISTS qa_review_cases (
    id BIGSERIAL PRIMARY KEY,
    qa_case_number VARCHAR(100) UNIQUE NOT NULL,
    original_case_number VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    selected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    selection_basis VARCHAR(255),
    assigned_reviewer_id VARCHAR(100),
    assigned_team_lead_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- QA Assignment Rules
CREATE TABLE IF NOT EXISTS qa_assignment_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    criteria JSONB NOT NULL,
    priority INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- QA Action Plans
CREATE TABLE IF NOT EXISTS qa_action_plans (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    plan_details TEXT NOT NULL,
    prepared_by VARCHAR(100) NOT NULL,
    approval_request_id BIGINT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- QA Findings
CREATE TABLE IF NOT EXISTS qa_findings (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    area_criterion_reviewed VARCHAR(255) NOT NULL,
    outcome VARCHAR(50) NOT NULL,
    evidence_notes TEXT,
    recorded_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- QA Reports
CREATE TABLE IF NOT EXISTS qa_reports (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    version VARCHAR(50) NOT NULL,
    summary TEXT NOT NULL,
    recommendations TEXT,
    approval_request_id BIGINT,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    adjusted_at TIMESTAMP
);

-- Exit Conference Agendas
CREATE TABLE IF NOT EXISTS exit_conference_agendas (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    prepared_by_role VARCHAR(50) NOT NULL,
    items JSONB NOT NULL,
    approval_request_id BIGINT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Exit Conferences
CREATE TABLE IF NOT EXISTS exit_conferences (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    scheduled_at TIMESTAMP,
    attendees JSONB,
    minutes_outcome_notes TEXT,
    conducted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Follow-up Actions
CREATE TABLE IF NOT EXISTS follow_up_actions (
    id BIGSERIAL PRIMARY KEY,
    qa_review_case_id BIGINT NOT NULL REFERENCES qa_review_cases(id),
    type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    assigned_to VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    determined_by VARCHAR(100) NOT NULL,
    determined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Follow-up Verifications
CREATE TABLE IF NOT EXISTS follow_up_verifications (
    id BIGSERIAL PRIMARY KEY,
    follow_up_action_id BIGINT NOT NULL REFERENCES follow_up_actions(id),
    verified_by VARCHAR(100) NOT NULL,
    verified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    outcome VARCHAR(50) NOT NULL,
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_qa_review_cases_status ON qa_review_cases(status);
CREATE INDEX IF NOT EXISTS idx_qa_review_cases_original_case ON qa_review_cases(original_case_number);
CREATE INDEX IF NOT EXISTS idx_qa_review_cases_reviewer ON qa_review_cases(assigned_reviewer_id);
CREATE INDEX IF NOT EXISTS idx_qa_findings_case ON qa_findings(qa_review_case_id);
CREATE INDEX IF NOT EXISTS idx_qa_reports_case ON qa_reports(qa_review_case_id);
CREATE INDEX IF NOT EXISTS idx_follow_up_actions_case ON follow_up_actions(qa_review_case_id);
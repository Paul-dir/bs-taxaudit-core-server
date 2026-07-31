# Tax Audit Service — System Reference

**Service**: `bs-taxaudit-core-server`
**Bounded context**: desk audit execution (start → evidence → sampling → findings → team leader review → finalize)
**Stack**: Java 21 · Spring Boot 3.3 · PostgreSQL 16 · Hibernate 6 · Flyway · Resilience4j · Springdoc OpenAPI
**Status**: Phase-1 complete (desk audit execution end-to-end through finalization).

---

## 1. What this service does

The tax audit service owns everything that happens **during a desk audit** — the structured, document-based examination of a taxpayer's records without physical inspection:

1. **Starts** a desk audit linked to an existing audit case.
2. **Gathers evidence** from internal systems and external sources.
3. **Requests supporting documents** from the taxpayer via DMS.
4. **Determines sampling method** when full population review is impractical.
5. **Records findings** with severity, area, and risk implications.
6. **Submits draft report** prepared by the audit officer.
7. **Team leader review** — approve/finalize or escalate to comprehensive audit.
8. **Fraud flagging** — suspends the audit and triggers investigation.
9. **Finalizes** the desk audit when approved.

It does **not** own:

- Audit case creation (case-management service)
- Comprehensive audit execution (comprehensive-audit service)
- Document storage (DMS service)
- Notification delivery (notification-engine)
- Payment/refund processing (payment-service)
- Fraud investigation (case-management)

These are deferred to Phase 2 — the hooks are in place (events, ports), but the use cases are not implemented.

---

## 2. Architecture

Strict hexagonal layering — six packages, no exceptions:

```
com.act.taxaudit/
├── api/             ← REST controllers, DTOs, RFC 7807 problem details
├── application/     ← @Service use cases, ports (interfaces), event handlers
├── domain/          ← Aggregates, value objects, domain events, exceptions
├── persistence/     ← JPA entities, repos, adapters implementing repository ports
├── engineadapter/   ← External-system adapters (mock today): notification, dms
└── observability/   ← Audit interceptor, MDC filter, correlation ID
```

There is **no security package**. Authentication and authorization are gateway-owned (Keycloak + API Gateway). Inside the service, identity comes from the `X-Actor-Id` header propagated by the gateway.

ArchUnit tests enforce:
1. Controllers can't import from `persistence` or `engineadapter`.
2. Domain has no JPA / Spring annotations.
3. Engine adapters aren't called from the API layer.
4. Persistence doesn't depend on application use cases.
5. No Spring Security classes anywhere.

### URL note

`server.servlet.context-path: /api/v1` is set, **and** every controller declares `@RequestMapping("/api/v1/...")`. So every URL is double-prefixed: `http://host:8080/api/v1/api/v1/<path>`. Future cleanup: drop one of the two prefixes; until then, all examples below show the full path you'd actually hit.

---

## 3. Domain model — aggregates

| Aggregate | What it represents | Key states |
|---|---|---|
| **DeskAudit** | The desk audit itself — evidence, sampling, findings, draft report, team leader decision, fraud flag. | `STARTED → EVIDENCE_GATHERING → DOCUMENTS_REQUESTED → SAMPLING_DETERMINED → FINDINGS_RECORDED → DRAFT_REPORT_SUBMITTED → {FINALIZED \| ESCALATED_TO_COMPREHENSIVE \| SUSPENDED_FRAUD_INVESTIGATION}` |
| **EvidenceItem** | A single piece of evidence collected during the audit. | entity |
| **DocumentRequest** | A request sent to the taxpayer for supporting documents. | entity |
| **DeskAuditFinding** | A finding recorded by the auditor with severity and area. | entity |
| **SampleSelection** | The sampling method and selected items for review. | value object |
| **DraftAuditReport** | The draft report prepared by the auditor. | value object |
| **FraudFlag** | A fraud indicator flagged during the audit. | value object |

### State machine — DeskAudit (the spine)

```
STARTED ──recordEvidence──> EVIDENCE_GATHERING
EVIDENCE_GATHERING ──requestDocuments──> DOCUMENTS_REQUESTED
DOCUMENTS_REQUESTED ──determineSampling──> SAMPLING_DETERMINED
SAMPLING_DETERMINED ──recordFindings──> FINDINGS_RECORDED
FINDINGS_RECORDED ──submitDraftReport──> DRAFT_REPORT_SUBMITTED
DRAFT_REPORT_SUBMITTED ──teamLeaderDecision(APPROVE_FINALIZE)──> FINALIZED
DRAFT_REPORT_SUBMITTED ──teamLeaderDecision(ESCALATE_COMPREHENSIVE)──> ESCALATED_TO_COMPREHENSIVE
DRAFT_REPORT_SUBMITTED ──flagFraud──> SUSPENDED_FRAUD_INVESTIGATION
SUSPENDED_FRAUD_INVESTIGATION ──resumeAfterFraudCleared──> FINDINGS_RECORDED
```

---

## 4. The end-to-end happy path

What actually happens when an auditor performs a desk audit:

1. **Audit case exists** — created by case-management service.
2. **Auditor starts desk audit** — `POST /desk-audits` with `auditCaseId` and `tin`. Status: `STARTED`.
3. **Gather evidence** — `POST /desk-audits/{id}/evidence` adds evidence items. Status: `EVIDENCE_GATHERING`.
4. **Request documents** — `POST /desk-audits/{id}/document-requests` sends requests to taxpayer via DMS. Status: `DOCUMENTS_REQUESTED`.
5. **Determine sampling** — `POST /desk-audits/{id}/sampling` selects sampling method and items. Status: `SAMPLING_DETERMINED`.
6. **Record findings** — `POST /desk-audits/{id}/findings` adds findings with severity. Status: `FINDINGS_RECORDED`.
7. **Submit draft report** — `POST /desk-audits/{id}/draft-report` submits the draft report. Status: `DRAFT_REPORT_SUBMITTED`.
8. **Team leader review** — `POST /desk-audits/{id}/team-leader-decision` with `APPROVE_FINALIZE` or `ESCALATE_COMPREHENSIVE`. Status: `FINALIZED` or `ESCALATED_TO_COMPREHENSIVE`.
9. **Fraud flag** (optional) — `POST /desk-audits/{id}/fraud-flag` suspends the audit. Status: `SUSPENDED_FRAUD_INVESTIGATION`.
10. **Finalize** — `POST /desk-audits/{id}/finalize` finalizes the audit. Status: `FINALIZED`.

---

## 5. API catalog

All paths shown are **the URL you actually hit** (with the double `/api/v1/api/v1/` prefix). `X-Actor-Id` header is required on every call.

### 5.1 Desk audits — the main aggregate

`@RequestMapping("/api/v1/desk-audits")`

| Method | Path | What it does |
|---|---|---|
| POST | `/api/v1/api/v1/desk-audits` | Starts a new desk audit. Body: `auditCaseId`, `tin`. Returns 201 Created. |
| POST | `/api/v1/api/v1/desk-audits/{id}/evidence` | Adds evidence item. Body: `actorId`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/document-requests` | Requests documents from taxpayer. Body: `documentTypes[]`, `requestedByActorId`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/sampling` | Determines sampling method. Body: `method`, `criteria`, `selectedItems[]`, `sampleSize`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/findings` | Records findings. Body: `findings[]` with `area`, `severity`, `description`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/draft-report` | Submits draft report. Body: `narrative`, `findingsSummary`, `preparedByActorId`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/team-leader-decision` | Team leader decision. Body: `decision` (`APPROVE_FINALIZE`/`ESCALATE_COMPREHENSIVE`), `actorId`, `narrative`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/fraud-flag` | Flags fraud. Body: `indicatorNotes`, `flaggedByActorId`. |
| POST | `/api/v1/api/v1/desk-audits/{id}/finalize` | Finalizes the audit. Body: `finalizedByActorId`. |
| GET | `/api/v1/api/v1/desk-audits/{id}` | Returns the desk audit summary. |

### 5.2 Webhooks — inter-module communication

`@RequestMapping("/api/v1/webhooks")`

| Method | Path | What it does |
|---|---|---|
| POST | `/api/v1/api/v1/webhooks/module-completion` | Receives async completion notifications from other ITAS modules. Body: `module`, `eventType`, `aggregateId`, `status`, `completedAt`. |
| GET | `/api/v1/api/v1/webhooks/health` | Health check endpoint. |

---

## 6. Headers, errors, observability

**Required headers** (all real requests, not actuator):

| Header | Required | Purpose |
|---|---|---|
| `X-Actor-Id` | yes (defaults to `"anonymous"`) | Whoever's making the call — auditor ID for back-office. Captured by `AuditInterceptor` and put into MDC. |
| `X-Correlation-Id` | optional (auto-generated) | Trace id propagated through logs and event chains. |
| `Content-Type: application/json` | yes for body-bearing methods | Standard. |

**Error response shape** — RFC 7807 `ProblemDetail` JSON:

| HTTP | `type` | When |
|---|---|---|
| 400 | `urn:itas:taxaudit:validation-error` | Request body / param validation failed (`@Valid`). Includes `violations` map. |
| 404 | `urn:itas:taxaudit:not-found` | Aggregate not found, OR URL doesn't match any controller. |
| 409 | `urn:itas:taxaudit:conflict` | Optimistic locking conflict (concurrent update). |
| 422 | `urn:itas:taxaudit:domain-error` | Business-rule violation (invalid status transition, etc.). |
| 502 | `urn:itas:taxaudit:engine-error` | Engine adapter failed — circuit-breaker fallback. |
| 500 | `urn:itas:taxaudit:internal-error` | Anything unexpected. |

**Observability**:
- Every use case execution is wrapped by `AuditInterceptor` (AOP at `application.usecase..*(..)`). Logs `START / SUCCESS / FAILURE` with actor + duration.
- `MdcContextFilter` puts `correlationId` + `actorId` into MDC; the log pattern echoes both. `traceId` comes from Micrometer Brave.
- Prometheus metrics include `http.server.requests` histograms.

---

## 7. Configuration

`src/main/resources/application.yml` — the highlights:

| Knob | Default | What it controls |
|---|---|---|
| `server.port` | `${SERVER_PORT:8080}` | HTTP port. |
| `server.servlet.context-path` | `/api/v1` | Half of the `/api/v1/api/v1` double-prefix. |
| `spring.threads.virtual.enabled` | `true` | Java 21 virtual threads for Tomcat + `@Async`. |
| `spring.datasource.url` | `jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:taxaudit_db}?stringtype=unspecified` | The `?stringtype=unspecified` is required so JSONB string writes auto-cast in PG. |
| `spring.jpa.hibernate.ddl-auto` | `validate` | Schema is owned by Flyway. |
| `itas.kafka.enabled` | `false` (local) | Enable Kafka event publishing. |
| `itas.kafka.bootstrap-servers` | `localhost:9092` | Kafka broker address. |
| `resilience4j.circuitbreaker.instances.<name>` | varies | One per engine adapter: `notification-engine`, `dms`. |

---

## 8. Scheduled jobs

| Job | Cadence | What it does |
|---|---|---|
| `OutboxRetryScheduler` | every 5s | Retries failed outbox entries (Kafka, DMS, etc.). |

---

## 9. Database tables

| Table | Owns | Notes |
|---|---|---|
| `desk_audits` | DeskAudit aggregate | One row per desk audit; evidence, findings, document requests stored as JSONB columns. Optimistic locking via `@Version`. |
| `outbox_entries` | OutboxEntry | At-least-once delivery for external calls (Kafka, DMS). |
| `flyway_schema_history` | Flyway | `V1__initial_schema.sql`. |

---

## 10. Engine adapters (all mocked today)

Every adapter extends `BaseEngineAdapter`, has `@CircuitBreaker(name=…, fallbackMethod=…)` + `@Retry(name=…)` on every `@Override` method, and has a one-line `[MOCK]` Javadoc.

| Adapter | Port | What it stubs out |
|---|---|---|
| `NotificationEngineMockAdapter` | `NotificationEnginePort` | Sends notifications to auditors and taxpayers. |
| `DmsEngineMockAdapter` | `DmsEnginePort` | Stores and retrieves documents from DMS. |

---

## 11. Channels — back-office only

| Channel | `X-Actor-Id` | UI |
|---|---|---|
| Auditor | auditor actor id | back-office UI (not built yet) — call same APIs via Swagger / Bruno today |
| Team leader | team-leader actor id | back-office UI |
| System | system actor | scheduled tasks |

`AuditInterceptor` already records `X-Actor-Id` correctly.

---

## 12. What's not in this service (Phase-2 hand-offs)

These were intentionally cut from tax-audit service per the refined design:

| Concern | Owner |
|---|---|
| Audit case creation and management | case-management |
| Comprehensive audit execution | comprehensive-audit |
| Document storage and retrieval | DMS |
| Notification delivery (email, SMS, in-app) | notification-engine |
| Fraud investigation post-flag | case-management |
| Payment/refund processing | payment-service |
| Taxpayer portal | portal-service |

Tax-audit service publishes the events these services consume (e.g. `DeskAuditFinalizedEvent`, `FraudIndicatorFlaggedEvent`, `TeamLeaderDeskAuditDecidedEvent`) via the outbox so the integrations are at-least-once.

---

## 13. Quick test — full happy path with curl

```bash
B="http://localhost:8080/api/v1/api/v1"
HDR_ACT="-H X-Actor-Id:auditor-001"
HDR_JSON="-H Content-Type:application/json"

# 1. Start a desk audit
DA=$(curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"auditCaseId":"12345678-1234-1234-1234-123456789012","tin":"0011223344"}' \
  "$B/desk-audits" | jq -r .id)

# 2. Gather evidence
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"actorId":"auditor-001"}' \
  "$B/desk-audits/$DA/evidence"

# 3. Request documents
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"documentTypes":["BANK_STATEMENT","INVOICE"],"requestedByActorId":"auditor-001"}' \
  "$B/desk-audits/$DA/document-requests"

# 4. Determine sampling
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"method":"RANDOM","criteria":"High-value transactions","selectedItems":["INV-001","INV-002"],"sampleSize":2}' \
  "$B/desk-audits/$DA/sampling"

# 5. Record findings
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"findings":[{"area":"VAT Reconciliation","severity":"MEDIUM","description":"Discrepancy in input VAT claims"}]}' \
  "$B/desk-audits/$DA/findings"

# 6. Submit draft report
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"narrative":"Audit completed","findingsSummary":"Minor discrepancies found","preparedByActorId":"auditor-001"}' \
  "$B/desk-audits/$DA/draft-report"

# 7. Team leader decision
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"decision":"APPROVE_FINALIZE","actorId":"team-leader-001","narrative":"Approved"}' \
  "$B/desk-audits/$DA/team-leader-decision"

# 8. Finalize
curl -s -X POST $HDR_ACT $HDR_JSON \
  -d '{"finalizedByActorId":"team-leader-001"}' \
  "$B/desk-audits/$DA/finalize"

# 9. Verify status
curl -s "$B/desk-audits/$DA" | jq .status  # → "FINALIZED"
```

---

*Last updated: 2026-07-31 — Phase-1 complete.*
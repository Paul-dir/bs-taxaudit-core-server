# Tax Audit Service — Agent/Developer Guidelines

This file provides operational context for AI agents and developers working on `bs-taxaudit-core-server`.

---

## 1. Service Purpose

The tax audit service manages **desk audit execution** — the structured, document-based examination of a taxpayer's records. It is part of the ITAS (Integrated Tax Administration System) ecosystem.

**Key workflows:**
- Start desk audit → gather evidence → request documents → determine sampling → record findings → submit draft report → team leader review → finalize/escalate/fraud flag

---

## 2. Architecture Rules

### 2.1 Hexagonal Layering (Strict)

```
com.act.taxaudit/
├── api/             ← REST controllers, DTOs, RFC 7807 problem details
├── application/     ← @Service use cases, ports (interfaces), event handlers
├── domain/          ← Aggregates, value objects, domain events, exceptions
├── persistence/     ← JPA entities, repos, adapters implementing repository ports
├── engineadapter/   ← External-system adapters (mock today): notification, dms
└── observability/   ← Audit interceptor, MDC filter, correlation ID
```

**Rules:**
1. Controllers can ONLY import from `application.usecase` and `api.dto`.
2. Domain has NO JPA / Spring annotations.
3. Engine adapters are ONLY called from `application.usecase`.
4. Persistence doesn't depend on application use cases.
5. NO Spring Security classes anywhere (gateway-owned).

### 2.2 DTO Pattern

- **Request DTOs** go in `api/dto/request/` — one file per endpoint.
- **Response DTOs** go in `api/dto/response/` — one file per aggregate/response shape.
- **Never** return domain objects (`DeskAudit`, `EvidenceItem`, etc.) directly from controllers.
- Use `DeskAuditResponse.from(deskAudit)` to convert domain → DTO.

### 2.3 Use Case Pattern

Every use case:
- Is a `@Service` class in `application.usecase`.
- Has a single `execute()` method (or overloads).
- Is transactional (`@Transactional`).
- Throws `ResourceNotFoundException` for missing aggregates.
- Throws `DomainException` for business rule violations.
- Returns the domain aggregate (controller converts to DTO).

### 2.4 Domain Events

- Domain events are registered via `registerEvent()` in aggregate methods.
- Events are dispatched by the outbox (Kafka/DMS).
- Event names follow pattern: `{AggregateName}{EventName}Event` (e.g., `DeskAuditFinalizedEvent`).

---

## 3. State Machine — DeskAudit

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

**Invalid transitions throw `DomainException` with message:**
- `"Cannot <action> in status: <currentStatus>"`
- `"Can only <action> when <condition>"`

---

## 4. API Conventions

### 4.1 URL Structure

- Base path: `/api/v1/desk-audits`
- All endpoints are under `/desk-audits` except webhooks (`/webhooks`).
- Path parameters use `{id}` (the desk audit UUID).

### 4.2 Headers

| Header | Required | Purpose |
|---|---|---|
| `X-Actor-Id` | yes | Auditor/team leader ID. Defaults to `"anonymous"`. |
| `X-Correlation-Id` | optional | Trace ID. Auto-generated if missing. |
| `Content-Type: application/json` | yes for POST/PUT | Standard. |

### 4.3 Response Format

All successful responses return `DeskAuditResponse`:
```json
{
  "id": "uuid",
  "auditCaseId": "uuid",
  "tin": "string",
  "status": "STARTED|EVIDENCE_GATHERING|...|FINALIZED",
  "evidenceIds": ["uuid"],
  "samplingMethod": "RANDOM|STATISTICAL|...",
  "findingCount": 2,
  "teamLeaderDecision": "APPROVE_FINALIZE|ESCALATE_COMPREHENSIVE|null",
  "escalationDecision": "APPROVED|null",
  "hasFraudFlag": false,
  "hasDraftReport": true,
  "documentRequestCount": 1,
  "createdAt": "2026-07-31T...",
  "updatedAt": "2026-07-31T..."
}
```

### 4.4 Error Responses

RFC 7807 `ProblemDetail`:
- `400` — validation error
- `404` — not found
- `409` — optimistic locking conflict
- `422` — domain error (invalid state transition)
- `502` — engine error
- `500` — internal error

---

## 5. Database Conventions

- **JSONB columns** for complex value objects: `evidence_items_json`, `findings_json`, `document_requests_json`, `draft_report_json`, `fraud_flag_json`.
- **Optimistic locking** via `@Version` on `version` column.
- **Flyway** for migrations — never use `hibernate.ddl-auto=update`.
- **Connection string** must include `?stringtype=unspecified` for JSONB auto-cast.

---

## 6. Testing Conventions

### 6.1 Unit Tests

- Test use cases in `application.usecase`.
- Mock repository ports.
- Verify domain events are registered.

### 6.2 Integration Tests

- Test controllers with `@WebMvcTest`.
- Use `@MockBean` for use cases.
- Verify HTTP status codes and response bodies.

### 6.3 E2E Tests

- Follow the pattern in `docs/E2E-TEST-SCENARIO-ATLAS.md`.
- Use Swagger UI or Bruno collection.
- Test happy path + negative paths.

---

## 7. Common Tasks

### 7.1 Add a new endpoint

1. Create request DTO in `api/dto/request/`.
2. Create/update use case in `application/usecase/`.
3. Add method to `DeskAuditController` returning `ResponseEntity<DeskAuditResponse>`.
4. Update `TAXAUDIT-SYSTEM.md` API catalog.
5. Add test case to `docs/E2E-TEST-SCENARIO-ATLAS.md`.

### 7.2 Add a new domain event

1. Create event class in `domain/event/` extending `DomainEvent`.
2. Register event in aggregate method via `registerEvent()`.
3. Create event handler in `application/event/` if needed.
4. Update outbox dispatcher if external delivery is required.

### 7.3 Add a new engine adapter

1. Create port interface in `application/port/`.
2. Create adapter in `engineadapter/` extending `BaseEngineAdapter`.
3. Add `@CircuitBreaker` + `@Retry` annotations.
4. Wire in `application.yml` under `resilience4j.circuitbreaker.instances.<name>`.

---

## 8. Pitfalls to Avoid

1. **Never return domain objects from controllers** — always use DTOs.
2. **Never call engine adapters from controllers** — use cases only.
3. **Never put JPA annotations in domain** — only in `persistence.jpa.entity`.
4. **Never skip `@Transactional` on use cases** — required for event registration.
5. **Never hardcode actor IDs** — use `X-Actor-Id` header.
6. **Never forget to save after domain method calls** — `repository.save(deskAudit)`.
7. **Never use `hibernate.ddl-auto=update`** — use Flyway migrations.
8. **Never commit without running `mvn compile` first** — verify no compilation errors.

---

## 9. Reference Files

| File | Purpose |
|---|---|
| `TAXAUDIT-SYSTEM.md` | Architecture overview, API catalog, configuration |
| `docs/E2E-TEST-SCENARIO-ATLAS.md` | End-to-end test scenarios |
| `docs/HOW_TO_RUN.md` | Setup and run instructions |
| `docs/DeskAudit_API_Collection.json` | Postman/Bruno collection |
| `Sample/bs-filling-core-server/bs-filing-core-server/FILING-SYSTEM.md` | Reference architecture (filing service) |
| `Sample/bs-filling-core-server/bs-filing-core-server/E2E-TEST-SCENARIO-ATLAS.md` | Reference E2E tests (filing service) |

---

## 10. Quick Commands

```bash
# Compile
mvn compile

# Run tests
mvn test

# Run service
mvn spring-boot:run

# Git commit (always include task_progress in commit message)
git add -A
git commit -m "feat: <description>

task_progress:
- [x] <completed item>
- [ ] <pending item>"
```

---

*Last updated: 2026-07-31*
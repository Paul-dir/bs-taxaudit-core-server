# ITAS Tax Audit Service (bs-taxaudit-core-server)

Desk Audit execution capability for the ITAS system, implementing SoR Module D requirements (FR-04.3-01..08 + UC-AP-009).

## Technology Stack

- **Spring Boot 3.3.0** with Java 21 (virtual threads enabled)
- **Apache Kafka** as the asynchronous event backbone (mandatory deviation from bs-filling pattern)
- **PostgreSQL 16** with Flyway migrations
- **Resilience4j** for circuit breaker and retry patterns
- **Spring Data JPA** with JSONB for nested collections
- **Micrometer + Prometheus** for metrics
- **Brave** for distributed tracing
- **Logstash** for JSON logging
- **MapStruct** for DTO mapping
- **Lombok** for boilerplate reduction
- **ArchUnit** for architecture enforcement
- **Testcontainers** for integration testing

## Architecture

This service follows the hexagonal/DDD pattern with a mandatory Kafka deviation:

```
api/                    # REST controllers (backoffice, portal, webhook)
application/
  ├── usecase/          # Use case implementations (one per business operation)
  ├── port/             # Port interfaces (driving & driven)
  ├── event/            # Event handlers (@EventListener/@TransactionalEventListener)
  ├── outbox/           # Outbox dispatcher (Kafka producer relay)
  └── scheduling/       # Scheduled jobs (reminders, etc.)
domain/
  ├── aggregate/        # AggregateRoot base + DeskAudit aggregate
  ├── model/            # Entity objects (EvidenceItem)
  ├── event/            # Domain events (immutable records)
  ├── valueobject/      # Value objects (DeskAuditFinding, etc.)
  └── exception/        # Domain exceptions
persistence/
  ├── jpa/              # JPA entities + repositories
  └── adapter/          # Repository adapter (implements port)
engineadapter/          # External service adapters (mock implementations)
serviceadapter/        # Service adapters (case management, fraud, etc.)
observability/         # MDC filters, audit interceptor, metrics
config/                # Configuration classes
```

### Key Design Decisions

1. **Kafka Event Backbone**: Unlike bs-filling-core-server which uses direct WebClient calls from the outbox, this service publishes domain events to Kafka topics. The outbox table still exists for reliability, but the dispatcher publishes to Kafka instead of calling engines directly.

2. **Synchronous Calls**: Direct request/response integrations (DMS, data warehouse, third-party data) remain as synchronous WebClient + Resilience4j calls. Kafka is only for fire-and-forget eventual consistency.

3. **Aggregate Pattern**: DeskAudit follows the same pattern as TaxReturn in bs-filling:
   - Private constructor + static factory methods
   - Business methods validate state and register domain events
   - Events are drained via `pullEvents()` after `repository.save()`
   - Optimistic locking via version column

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- Docker & Docker Compose
- Git

### Running Locally

1. **Start infrastructure** (PostgreSQL + Kafka):
   ```bash
   docker-compose up -d
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   Or with specific profiles:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
   ```

3. **Access services**:
   - API: http://localhost:8082/api/v1
   - Swagger UI: http://localhost:8082/api/v1/swagger-ui.html
   - Kafka UI: http://localhost:8080
   - Actuator: http://localhost:8082/api/v1/actuator

### Running Tests

```bash
# Unit tests
mvn test

# Integration tests (requires Docker)
mvn verify

# Specific test class
mvn test -Dtest=DeskAuditAggregateTest
```

### Kafka Topics

The service publishes to the following topics (all with 3 partitions, keyed by deskAuditId):

- `taxaudit.deskaudit.documents-requested.v1`
- `taxaudit.deskaudit.reminder.v1`
- `taxaudit.deskaudit.draft-report-ready.v1`
- `taxaudit.deskaudit.risk-profile-update.v1`
- `taxaudit.deskaudit.case-status-changed.v1`
- `taxaudit.deskaudit.fraud-flagged.v1`

Each topic has a corresponding DLT (Dead Letter Topic): `<topic-name>.DLT`

## Project Structure

```
bs-taxaudit-core-server/
├── src/
│   ├── main/
│   │   ├── java/com/act/taxaudit/
│   │   │   ├── TaxAuditServiceApplication.java
│   │   │   ├── api/
│   │   │   ├── application/
│   │   │   ├── config/
│   │   │   ├── domain/
│   │   │   ├── engineadapter/
│   │   │   ├── observability/
│   │   │   └── persistence/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── logback-spring.xml
│   │       └── db/migration/  (Flyway)
│   └── test/
│       ├── java/  (unit + integration tests)
│       └── resources/
├── pom.xml
├── docker-compose.yml
└── README.md
```

## API Endpoints

### Backoffice (Auditor/Team Leader/Director)
- `POST /api/v1/desk-audits` - Start desk audit
- `POST /api/v1/desk-audits/{id}/evidence` - Gather evidence
- `POST /api/v1/desk-audits/{id}/document-requests` - Request documents from taxpayer
- `POST /api/v1/desk-audits/{id}/documents` - Receive taxpayer document (portal)
- `POST /api/v1/desk-audits/{id}/sampling` - Determine sampling method
- `POST /api/v1/desk-audits/{id}/findings` - Record findings
- `POST /api/v1/desk-audits/{id}/draft-report` - Submit draft report
- `POST /api/v1/desk-audits/{id}/team-leader-decision` - Team leader review
- `POST /api/v1/desk-audits/{id}/escalate` - Escalate to comprehensive audit
- `POST /api/v1/desk-audits/{id}/fraud-flag` - Flag fraud indicator
- `GET /api/v1/desk-audits/{id}` - Get desk audit details
- `GET /api/v1/desk-audits/case/{caseId}` - List desk audits for case

## Desk Audit State Machine

```
STARTED
  ↓
EVIDENCE_GATHERING
  ↓
DOCUMENTS_REQUESTED
  ↓
SAMPLING_DETERMINED
  ↓
FINDINGS_RECORDED
  ↓
DRAFT_REPORT_SUBMITTED
  ↓
  ├─→ FINALIZED (team leader approves)
  ├─→ ESCALATED_TO_COMPREHENSIVE (director approves escalation)
  └─→ SUSPENDED_FRAUD_INVESTIGATION (fraud flag)
```

## Development

### Code Style
- Follow hexagonal/DDD layering strictly
- Domain layer must not import persistence or engineadapter packages (enforced by ArchUnit)
- All business logic in domain aggregate, not in use cases
- Use cases orchestrate, aggregates decide

### Commit Convention
- `feat(domain):` - Domain layer changes
- `feat(application):` - Use cases and ports
- `feat(kafka):` - Kafka infrastructure
- `feat(persistence):` - Database and repositories
- `feat(adapters):` - Engine/service adapters
- `feat(api):` - Controllers and DTOs
- `feat(events):` - Event handlers
- `chore(observability):` - Logging, tracing, metrics
- `test:` - Test additions
- `docs:` - Documentation

## License

Proprietary - ITAS Project
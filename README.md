# bs-taxaudit-core-server

ITAS Tax Audit Core Service - Desk Audit Execution Module

## Overview
This service implements the Desk Audit execution capability for the ITAS system, following a phased hexagonal architecture with Kafka-based event-driven communication.

## Architecture
- **Domain Layer**: DeskAudit aggregate, 12 domain events, value objects, exceptions
- **Application Layer**: 10 ports, 6 use cases, event handlers, scheduled jobs
- **Infrastructure Layer**: Kafka topics (6 topics + 6 DLTs), Flyway migrations, mock adapters
- **API Layer**: REST controllers with ProblemDetail error responses

## Key Features
- Desk audit lifecycle management (STARTED → EVIDENCE_GATHERING → DOCUMENTS_REQUESTED → SAMPLING_DETERMINED → FINDINGS_RECORDED → DRAFT_REPORT_SUBMITTED → FINALIZED/ESCALATED/SUSPENDED)
- Evidence gathering from internal data warehouse and third-party sources
- Document request management with reminder notifications
- Sampling method determination
- Findings recording and draft report submission
- Team leader decision workflow
- Fraud flagging and escalation
- Transactional outbox pattern for reliable event publishing
- Resilience4j circuit breakers and retries for external service calls

## Technology Stack
- Spring Boot 3.x
- PostgreSQL with Flyway migrations
- Kafka for event streaming
- Resilience4j for fault tolerance
- Micrometer for observability

## Getting Started
See `docker-compose.yml` for infrastructure dependencies.

## Branch
`feature/desk-audit-execution`
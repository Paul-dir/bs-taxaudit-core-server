## 👥 Team Members & Assigned Implementation Areas

The Tax Audit Core Server is developed collaboratively. Each team member owns one or more business clusters and is responsible for delivering the complete implementation of those modules, including domain model, application layer, REST APIs, persistence, integrations, testing, and documentation.

| Team Member | Assigned Cluster(s) | Primary Responsibilities |
|--------------|---------------------|--------------------------|
| **Pawlos** | **Cluster AP – Audit Planning & Setup** | Implement Annual Audit Planning, Audit Case Selection, Audit Case Assignment, Audit Planning, and related workflows, APIs, domain models, persistence, and integrations. |
| **Yoseph** | **Cluster JA – Joint Audit**<br>**Cluster CM – Communication & Taxpayer Portal**<br>**Cluster RF – Reporting & Finalization** | Implement Joint Audit processes, Taxpayer Communication, Portal interactions, Audit Notices, Reporting, Assessment Notices, Audit Closure, and related integrations. |
| **Oliad** | **Cluster EX – Audit Execution (Desk & Comprehensive)**<br>Cluster QA – Quality Assurance & Oversight** | Implement Desk Audit, Comprehensive Audit,  Quality Assurance Reviews, Fraud Escalation, workflow orchestration, and business validations. |
| **Borifa** | **Cluster TP – Transfer Pricing Audit**<br>**Cluster IA – Issue Audit**<br>** | Implement the complete Transfer Pricing Audit module including planning, fieldwork, transfer, pricing analysis, reporting, approval workflow, and external integrations,Issue Audit lifecycle. |

---

### Expected Deliverables for Each Team Member

Each assigned cluster should include the following implementation:

- Domain Aggregates and Value Objects
- Domain Services and Business Rules
- Application Use Cases
- Commands, Queries, and DTOs
- REST Controllers (Back Office, Portal, Internal)
- Repository Interfaces and JPA Implementations
- Database Entities and Flyway Migrations
- Engine Adapters and External Integrations
- Domain Events and Event Publishing
- Validation Rules
- Unit Tests and Integration Tests
- API Documentation
- Technical Documentation

---

### Ownership Principle

Each team member is the **primary owner** of their assigned cluster and is responsible for:

- Designing the architecture of the module.
- Implementing all business requirements.
- Maintaining coding standards.
- Writing tests.
- Updating documentation.
- Reviewing integration points with other clusters.
- Supporting bug fixes and future enhancements.

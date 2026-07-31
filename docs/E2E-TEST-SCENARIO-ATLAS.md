# End-to-End Desk Audit Test — Atlas Computer Technologies (VAT, April 2026)

This document walks the full desk audit happy-path for a real-looking taxpayer using Swagger UI on `bs-taxaudit-core-server`. Copy each request body into Swagger and run them in order. Each step calls out the **id you must capture** from the response and re-use in the next step.

## Test taxpayer

| Field | Value |
|---|---|
| Legal name | **Atlas Computer Technologies PLC** |
| Type | `PRIVATE_LIMITED_COMPANY` (software development) |
| TIN | `0011223344` |
| Audit case id | `audit-case-001` |
| Period under test | **April 2026** |
| Tax type | `VAT` |

## Prerequisites

1. **Run the seed** — applies the desk audit schema so the calls below resolve cleanly.
    ```bash
    psql -h localhost -U taxaudit_user -d taxaudit_db \
      -f src/main/resources/db/seed/local-test-seed.sql
    ```
2. **Boot the service** —
    ```bash
    mvn spring-boot:run
    ```
3. **Open Swagger UI** — http://localhost:8080/swagger-ui/index.html
4. **Set a default header on every request** —
    - `X-Actor-Id: auditor-001`
    - `X-Correlation-Id: <new uuid per call>` (Swagger has a button; otherwise paste any uuid)

---

## Step 0 — sanity: see what's available

### 0.1 Webhook health check

`GET /api/v1/webhooks/health`

**Expected 200**:
```json
"Tax Audit module is running"
```

---

## Step 1 — Start the desk audit  (BUC-AUD-001)

`POST /api/v1/desk-audits`

```json
{
  "auditCaseId": "12345678-1234-1234-1234-123456789012",
  "tin": "0011223344"
}
```

**Expected 201 Created**:
```json
{
  "id": "<generated-uuid>",
  "auditCaseId": "12345678-1234-1234-1234-123456789012",
  "tin": "0011223344",
  "status": "STARTED",
  "evidenceIds": [],
  "samplingMethod": null,
  "findingCount": 0,
  "teamLeaderDecision": null,
  "escalationDecision": null,
  "hasFraudFlag": false,
  "hasDraftReport": false,
  "documentRequestCount": 0,
  "createdAt": "...",
  "updatedAt": "..."
}
```

> **Capture:** `deskAuditId = <id from response>`

---

## Step 2 — Gather evidence  (BUC-AUD-002)

`POST /api/v1/desk-audits/{deskAuditId}/evidence`

```json
{
  "actorId": "auditor-001"
}
```

**Expected 200** — status moves to `EVIDENCE_GATHERING`, evidenceIds populated.

> **Capture:** `evidenceId = <first evidence id from response.evidenceIds>`

---

## Step 3 — Request supporting documents  (BUC-AUD-003)

`POST /api/v1/desk-audits/{deskAuditId}/document-requests`

```json
{
  "documentTypes": ["BANK_STATEMENT", "INVOICE", "PURCHASE_ORDER"],
  "requestedByActorId": "auditor-001"
}
```

**Expected 200** — status moves to `DOCUMENTS_REQUESTED`, documentRequestCount = 1.

---

## Step 4 — Determine sampling method  (BUC-AUD-004)

`POST /api/v1/desk-audits/{deskAuditId}/sampling`

```json
{
  "method": "RANDOM",
  "criteria": "High-value transactions above ETB 100,000",
  "selectedItems": ["INV-ATL-2026-0401", "INV-ATL-2026-0402"],
  "sampleSize": 2
}
```

**Expected 200** — status moves to `SAMPLING_DETERMINED`, samplingMethod = `RANDOM`.

---

## Step 5 — Record findings  (BUC-AUD-005)

`POST /api/v1/desk-audits/{deskAuditId}/findings`

```json
{
  "findings": [
    {
      "area": "VAT Reconciliation",
      "severity": "MEDIUM",
      "description": "Discrepancy in input VAT claims for April 2026",
      "requiresRiskUpdate": true
    },
    {
      "area": "Purchase Verification",
      "severity": "LOW",
      "description": "Missing purchase orders for 2 invoices",
      "requiresRiskUpdate": false
    }
  ]
}
```

**Expected 200** — status moves to `FINDINGS_RECORDED`, findingCount = 2.

---

## Step 6 — Submit draft report  (BUC-AUD-006)

`POST /api/v1/desk-audits/{deskAuditId}/draft-report`

```json
{
  "narrative": "Desk audit completed for Atlas Computer Technologies PLC for April 2026 VAT period. Minor discrepancies found in input VAT claims and missing documentation for certain purchases.",
  "findingsSummary": "2 findings: 1 MEDIUM (VAT reconciliation), 1 LOW (missing purchase orders). Total potential VAT adjustment: ETB 15,000.",
  "preparedByActorId": "auditor-001"
}
```

**Expected 200** — status moves to `DRAFT_REPORT_SUBMITTED`, hasDraftReport = true.

---

## Step 7 — Team leader review — approve  (BUC-AUD-007)

`POST /api/v1/desk-audits/{deskAuditId}/team-leader-decision`

```json
{
  "decision": "APPROVE_FINALIZE",
  "actorId": "team-leader-001",
  "narrative": "Approved. Findings are substantiated and within acceptable tolerance."
}
```

**Expected 200** — status moves to `FINALIZED`, teamLeaderDecision = `APPROVE_FINALIZE`.

> **Alternative:** Use `"decision": "ESCALATE_COMPREHENSIVE"` to escalate to comprehensive audit. Status moves to `ESCALATED_TO_COMPREHENSIVE`.

---

## Step 8 — Verify the final state

`GET /api/v1/desk-audits/{deskAuditId}`

**Expected 200**:
```json
{
  "id": "<deskAuditId>",
  "status": "FINALIZED",
  "findingCount": 2,
  "hasDraftReport": true,
  "teamLeaderDecision": "APPROVE_FINALIZE",
  "teamLeaderActorId": "team-leader-001",
  "teamLeaderNarrative": "Approved. Findings are substantiated...",
  "teamLeaderDecidedAt": "...",
  "hasFraudFlag": false,
  "documentRequestCount": 1
}
```

---

## Step 9 — Test fraud flagging (optional negative path)

If you want to test the fraud flag flow, restart from Step 1 and use this decision instead:

`POST /api/v1/desk-audits/{deskAuditId}/team-leader-decision`

```json
{
  "decision": "ESCALATE_COMPREHENSIVE",
  "actorId": "team-leader-001",
  "narrative": "Complex fraud indicators detected"
}
```

Then flag fraud:

`POST /api/v1/desk-audits/{deskAuditId}/fraud-flag`

```json
{
  "indicatorNotes": "Suspected circular trading and fake invoices detected",
  "flaggedByActorId": "team-leader-001"
}
```

**Expected 200** — status moves to `SUSPENDED_FRAUD_INVESTIGATION`, hasFraudFlag = true.

---

## Verification snippets — peek the database

```sql
-- Desk audit state
SELECT id, audit_case_id, tin, status, version, updated_at
  FROM desk_audits WHERE tin = '0011223344';

-- Evidence items
SELECT jsonb_array_length(evidence_items_json::jsonb) AS evidence_count
  FROM desk_audits WHERE id = '<deskAuditId>';

-- Findings
SELECT jsonb_array_length(findings_json::jsonb) AS findings_count
  FROM desk_audits WHERE id = '<deskAuditId>';

-- Document requests
SELECT jsonb_array_length(document_requests_json::jsonb) AS doc_requests_count
  FROM desk_audits WHERE id = '<deskAuditId>';

-- Outbox entries fired by the flow
SELECT topic, status, attempts, last_error, created_at
  FROM outbox_entries
  WHERE aggregate_id = '<deskAuditId>'
  ORDER BY created_at;
```

---

## Negative-path tests (run after the happy path)

### A — requesting documents in wrong status

After Step 2, try Step 3 again. **Expected 422** with `domain.invalid_transition` and a message like *"Cannot request documents in status EVIDENCE_GATHERING"*.

### B — recording findings before sampling

After Step 3, try Step 5. **Expected 422** with `domain.invalid_transition` and a message like *"Cannot record findings in status DOCUMENTS_REQUESTED"*.

### C — team leader decision without draft report

After Step 5, try Step 7. **Expected 422** with `domain.invalid_transition` and a message like *"Team leader can only decide on draft report in DRAFT_REPORT_SUBMITTED status"*.

### D — finalizing without team leader approval

After Step 6, try Step 8 (finalize). **Expected 422** with `domain.invalid_transition` and a message like *"Can only finalize when team leader decision is APPROVE_FINALIZE"*.

### E — invalid TIN

`POST /api/v1/desk-audits` with `"tin": ""`. **Expected 422** with `domain.taxpayer_not_found` or validation error.

---

## What the end-to-end path proves

✅ **State machine** — DeskAudit transitions through all valid states.
✅ **Evidence gathering** — evidence items are recorded and tracked.
✅ **Document requests** — document requests are created and counted.
✅ **Sampling** — sampling method and selection are stored.
✅ **Findings** — findings with severity and area are recorded.
✅ **Draft report** — draft report is submitted with narrative and summary.
✅ **Team leader review** — decision is recorded with actor and narrative.
✅ **Fraud flagging** — fraud flag suspends the audit.
✅ **Finalization** — audit is finalized with proper validation.
✅ **Domain events** — events are registered for each state transition.
✅ **Optimistic locking** — version is incremented on each update.

Once this scenario is green end-to-end, tax-audit service is ready for integration testing with case-management, DMS, and notification-engine for the next stage of the flow (case escalation, document retrieval, notifications).

---

*Last updated: 2026-07-31 — Phase-1 complete.*
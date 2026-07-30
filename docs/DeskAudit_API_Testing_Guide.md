# Desk Audit API Testing Guide

## Prerequisites

1. **Start the application:**
   ```bash
   cd bs-taxaudit-core-server
   ./mvnw spring-boot:run
   ```

2. **Verify it's running:**
   - Open browser: http://localhost:8080/actuator/health
   - Should see: `{"status":"UP"}`

3. **Import Postman Collection:**
   - Open Postman
   - Click "Import" → "Upload Files"
   - Select: `DeskAudit_API_Collection.json`
   - All endpoints will be imported

---

## API Endpoints

### Base URL
```
http://localhost:8080
```

### 1. Start Desk Audit
**POST** `/desk-audits`

**Description:** Creates a new desk audit in STARTED status

**Request Body:**
```json
{
  "auditCaseId": "12345678-1234-1234-1234-123456789012",
  "tin": "1234567890"
}
```

**Expected Response (201 Created):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "auditCaseId": "12345678-1234-1234-1234-123456789012",
  "tin": "1234567890",
  "status": "STARTED",
  "evidenceItems": [],
  "findings": [],
  "documentRequests": [],
  "createdAt": "2026-01-15T10:30:00",
  "updatedAt": "2026-01-15T10:30:00"
}
```

---

### 2. Gather Evidence
**POST** `/desk-audits/{id}/evidence`

**Description:** Gathers evidence from data warehouse and third-party sources

**Path Parameter:**
- `id`: Desk Audit ID (from step 1)

**Request Body:**
```json
{
  "actorId": "auditor-001"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "status": "EVIDENCE_GATHERING",
  "evidenceItems": [
    {
      "evidenceId": "ev-001",
      "sourceType": "DATA_WAREHOUSE",
      "description": "Taxpayer income data",
      "collectedAt": "2026-01-15T10:31:00"
    }
  ],
  ...
}
```

---

### 3. Request Supporting Documents
**POST** `/desk-audits/{id}/document-requests`

**Description:** Requests documents from taxpayer

**Path Parameter:**
- `id`: Desk Audit ID

**Request Body:**
```json
{
  "documentTypes": ["INVOICE", "RECEIPT", "BANK_STATEMENT"],
  "requestedByActorId": "auditor-001"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "status": "DOCUMENTS_REQUESTED",
  "documentRequests": [
    {
      "requestId": "req-001",
      "type": "MIXED",
      "description": "Multiple documents requested",
      "requestedAt": "2026-01-15T10:32:00",
      "requestedBy": "auditor-001",
      "dueDate": "2026-01-22T10:32:00",
      "reminderCount": 0
    }
  ],
  ...
}
```

**Note:** This triggers a Kafka event that sends notification to taxpayer

---

### 4. Determine Sampling Method
**POST** `/desk-audits/{id}/sampling`

**Description:** Sets the sampling method for the audit

**Path Parameter:**
- `id`: Desk Audit ID

**Request Body:**
```json
{
  "method": "STATISTICAL_SAMPLING",
  "criteria": "Random sample of 100 transactions",
  "selectedItems": ["txn-001", "txn-002", "txn-003"],
  "sampleSize": 100
}
```

**Expected Response (200 OK):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "status": "SAMPLING_DETERMINED",
  "samplingMethod": "STATISTICAL_SAMPLING",
  "sampleSelection": {
    "method": "STATISTICAL_SAMPLING",
    "criteria": "Random sample of 100 transactions",
    "selectedItems": ["txn-001", "txn-002", "txn-003"],
    "sampleSize": 100
  },
  ...
}
```

---

### 5. Record Findings
**POST** `/desk-audits/{id}/findings`

**Description:** Records audit findings

**Path Parameter:**
- `id`: Desk Audit ID

**Request Body:**
```json
{
  "findings": [
    {
      "findingId": "find-001",
      "category": "INCOME_UNDERREPORTING",
      "severity": "HIGH",
      "description": "Taxpayer underreported income by 30%",
      "amount": 50000.00,
      "supportingEvidence": ["ev-001", "ev-002"]
    }
  ]
}
```

**Expected Response (200 OK):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "status": "FINDINGS_RECORDED",
  "findings": [
    {
      "findingId": "find-001",
      "category": "INCOME_UNDERREPORTING",
      "severity": "HIGH",
      "description": "Taxpayer underreported income by 30%",
      "amount": 50000.00,
      "supportingEvidence": ["ev-001", "ev-002"]
    }
  ],
  ...
}
```

---

### 6. Get Desk Audit
**GET** `/desk-audits/{id}`

**Description:** Retrieves current state of desk audit

**Path Parameter:**
- `id`: Desk Audit ID

**Expected Response (200 OK):**
```json
{
  "id": "87654321-4321-4321-4321-210987654321",
  "status": "FINDINGS_RECORDED",
  ...
}
```

---

## Complete Testing Flow

### Step-by-Step Test Scenario

**Step 1: Start Desk Audit**
```
POST http://localhost:8080/desk-audits
Body: {"auditCaseId": "12345678-1234-1234-1234-123456789012", "tin": "1234567890"}
→ Save the "id" from response
```

**Step 2: Gather Evidence**
```
POST http://localhost:8080/desk-audits/{id}/evidence
Body: {"actorId": "auditor-001"}
→ Status should be EVIDENCE_GATHERING
```

**Step 3: Request Documents**
```
POST http://localhost:8080/desk-audits/{id}/document-requests
Body: {"documentTypes": ["INVOICE", "RECEIPT"], "requestedByActorId": "auditor-001"}
→ Status should be DOCUMENTS_REQUESTED
→ Check logs for notification sent
```

**Step 4: Determine Sampling**
```
POST http://localhost:8080/desk-audits/{id}/sampling
Body: {
  "method": "STATISTICAL_SAMPLING",
  "criteria": "Random sample",
  "selectedItems": ["item1", "item2"],
  "sampleSize": 2
}
→ Status should be SAMPLING_DETERMINED
```

**Step 5: Record Findings**
```
POST http://localhost:8080/desk-audits/{id}/findings
Body: {
  "findings": [
    {
      "findingId": "find-001",
      "category": "INCOME_UNDERREPORTING",
      "severity": "HIGH",
      "description": "Underreported income",
      "amount": 50000.00,
      "supportingEvidence": []
    }
  ]
}
→ Status should be FINDINGS_RECORDED
```

**Step 6: Get Current State**
```
GET http://localhost:8080/desk-audits/{id}
→ Review complete state
```

---

## Monitoring & Observability

### Check Application Logs
```bash
# Windows
type logs/taxaudit.log

# Linux/Mac
tail -f logs/taxaudit.log
```

**Look for:**
- `DeskAuditStartedEvent` - audit started
- `EvidenceGatheredEvent` - evidence collected
- `DocumentsRequestedFromTaxpayerEvent` - documents requested
- `SamplingMethodSelectedEvent` - sampling determined
- `DeskAuditFindingsRecordedEvent` - findings recorded

### Check Kafka Events (if Kafka is running)
```bash
# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Should see:
# - taxaudit.deskaudit.documents-requested.v1
# - taxaudit.deskaudit.documents-requested.v1.DLT
# - taxaudit.deskaudit.reminder-sent.v1
# - taxaudit.deskaudit.draft-report-ready.v1
# - taxaudit.deskaudit.risk-profile-update.v1
# - taxaudit.deskaudit.case-status-changed.v1
# - taxaudit.deskaudit.fraud-flagged.v1
```

### Check Database (if PostgreSQL is running)
```sql
-- Connect to database
psql -U postgres -d taxaudit

-- Check desk audits
SELECT id, tin, status, created_at FROM desk_audits;

-- Check outbox events
SELECT aggregate_type, event_type, status, created_at 
FROM outbox_entries 
ORDER BY created_at DESC;

-- Check processed events
SELECT event_id, consumer_group, processed_at 
FROM processed_events;
```

---

## Error Scenarios to Test

### 1. Invalid Status Transition
```
Try to request documents when status is FINDINGS_RECORDED
→ Should get 400 Bad Request
```

### 2. Not Found
```
GET http://localhost:8080/desk-audits/00000000-0000-0000-0000-000000000000
→ Should get 404 Not Found
```

### 3. Invalid Request
```
POST http://localhost:8080/desk-audits
Body: {"auditCaseId": "", "tin": ""}
→ Should get 400 Bad Request (validation error)
```

---

## Quick Test Script

Use this to quickly test all endpoints:

```bash
# 1. Start audit
AUDIT=$(curl -X POST http://localhost:8080/desk-audits \
  -H "Content-Type: application/json" \
  -d '{"auditCaseId":"12345678-1234-1234-1234-123456789012","tin":"1234567890"}')

echo $AUDIT

# Extract ID (requires jq)
AUDIT_ID=$(echo $AUDIT | jq -r '.id')
echo "Audit ID: $AUDIT_ID"

# 2. Gather evidence
curl -X POST http://localhost:8080/desk-audits/$AUDIT_ID/evidence \
  -H "Content-Type: application/json" \
  -d '{"actorId":"auditor-001"}'

# 3. Request documents
curl -X POST http://localhost:8080/desk-audits/$AUDIT_ID/document-requests \
  -H "Content-Type: application/json" \
  -d '{"documentTypes":["INVOICE","RECEIPT"],"requestedByActorId":"auditor-001"}'

# 4. Get audit
curl http://localhost:8080/desk-audits/$AUDIT_ID
```

---

## Troubleshooting

### Application won't start
- Check if port 8080 is already in use
- Check logs in `logs/taxaudit.log`
- Verify Java 17+ is installed: `java -version`

### 404 Not Found
- Ensure application is running
- Check base URL is `http://localhost:8080`
- Verify endpoint path is correct

### 500 Internal Server Error
- Check application logs
- Verify database is running (if using PostgreSQL)
- Verify Kafka is running (if using event publishing)

### Events not being published
- Check if outbox dispatcher is running
- Verify Kafka is running
- Check application logs for errors
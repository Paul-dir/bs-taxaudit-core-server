# How to Run the Desk Audit Application

## The Problem
You got: `FATAL: password authentication failed for user "root"`
This means PostgreSQL is NOT running.

## Solution: Start PostgreSQL and Kafka

### Step 1: Start Docker Containers
```bash
cd bs-taxaudit-core-server
docker-compose up -d
```

### Step 2: Wait 30 Seconds
Let PostgreSQL and Kafka start up.

### Step 3: Verify PostgreSQL is Running
```bash
docker-compose ps
```
You should see 3 containers with status "Up".

### Step 4: Start the Application
```bash
cd bs-taxaudit-core-server
./mvnw spring-boot:run
```

### Step 5: Test
```bash
curl -X POST http://localhost:8082/api/v1/desk-audits \
  -H "Content-Type: application/json" \
  -d "{\"auditCaseId\":\"12345678-1234-1234-1234-123456789012\",\"tin\":\"1234567890\"}"
```

## If You Get Errors

### Port 5432 already in use
```bash
docker-compose down
docker-compose up -d
```

### Application won't start
Make sure Docker is running first!

## Services
- Application: http://localhost:8082/api/v1
- Kafka UI: http://localhost:8080
- PostgreSQL: localhost:5432 (user: root, pass: root@1234)
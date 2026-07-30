# How to Run WITHOUT Docker

## Prerequisites
- PostgreSQL running locally on port 5432
- Kafka running locally on port 9092 (optional - app works without it)
- Java 17+
- Maven

## Step 1: Setup PostgreSQL

Create database:
```sql
CREATE DATABASE taxaudit_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE taxaudit_db TO postgres;
```

## Step 2: Start the Application

```bash
cd bs-taxaudit-core-server
./mvnw spring-boot:run --args="--spring.profiles.active=local"
```

Or on Windows:
```bash
mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

## Step 3: Test

```bash
curl -X POST http://localhost:8080/api/v1/desk-audits \
  -H "Content-Type: application/json" \
  -d "{\"auditCaseId\":\"12345678-1234-1234-1234-123456789012\",\"tin\":\"1234567890\"}"
```

## Configuration

The app uses `application-local.yml`:
- PostgreSQL: localhost:5432, user: postgres, pass: postgres
- Kafka: localhost:9092 (if not running, app still works)
- Port: 8080

## Verify

- Health: http://localhost:8080/api/v1/actuator/health
- API: http://localhost:8080/api/v1/desk-audits
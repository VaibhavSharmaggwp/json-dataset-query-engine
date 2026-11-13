# 🚀 JSON Dataset Query Engine

A robust, REST-compliant Spring Boot application designed to ingest arbitrary semi-structured JSON records and perform dynamic **Group-By** and **Sort-By** queries over relational database storage.

---

## 📌 Problem Statement & Architecture

This application manages dynamic JSON datasets without rigid, predefined domain models. Key capabilities include:
- **Dynamic Ingestion**: Store arbitrary JSON documents under named datasets.
- **Dynamic Group-By**: Aggregate records by any JSON key dynamically at query time.
- **Dynamic Sort-By**: Sort records by any JSON field in ascending (`asc`) or descending (`desc`) order.
- **Relational Persistence**: Leverages **PostgreSQL** with Flyway database migrations for reliable persistence and JSON querying.
- **TDD & Clean Design**: Layered architecture (Controller, Service, Repository) built with testability in mind.

---

## 🛠 Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.4.2 (Spring Web, Spring Data JPA, Bean Validation)
- **Database:** PostgreSQL (with `jsonb` support)
- **Database Migrations:** Flyway
- **Build Tool:** Maven (Maven Wrapper included)
- **Testing:** JUnit 5, MockMvc, AssertJ

---

## 🚀 Getting Started

### 1. Prerequisites
- **JDK 17** or higher
- **PostgreSQL** running locally or in Docker
- **Maven** (or use the included `./mvnw` wrapper)

### 2. Database Configuration
Update the database connection details in [`src/main/resources/application.yml`](src/main/resources/application.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/json_engine_db
    username: postgres
    password: your_password
```

Ensure the database `json_engine_db` exists in PostgreSQL:
```sql
CREATE DATABASE json_engine_db;
```

### 3. Build & Run the Application
Using the Maven wrapper:

```bash
# On Linux / macOS
./mvnw clean spring-boot:run

# On Windows (PowerShell or CMD)
.\mvnw.cmd clean spring-boot:run
```

The application will start on `http://localhost:8080`.

---

## 📖 API Documentation

### 1. Insert Record API
Persists an arbitrary JSON record into a specified dataset.

- **URL:** `POST /api/dataset/{datasetName}/record`
- **Headers:** `Content-Type: application/json`

#### Request Example:
```bash
curl -X POST "http://localhost:8080/api/dataset/employee_dataset/record" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "John Doe",
    "age": 30,
    "department": "Engineering"
  }'
```

#### Response Example (`200 OK` or `201 Created`):
```json
{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}
```

---

### 2. Query API with Group-By
Groups dataset records by a specified top-level JSON attribute.

- **URL:** `GET /api/dataset/{datasetName}/query?groupBy={attribute}`

#### Request Example:
```bash
curl -X GET "http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department"
```

#### Response Example (`200 OK`):
```json
{
  "groupedRecords": {
    "Engineering": [
      { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" },
      { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" }
    ],
    "Marketing": [
      { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" }
    ]
  }
}
```

---

### 3. Query API with Sort-By
Sorts dataset records by a specified JSON attribute in ascending or descending order.

- **URL:** `GET /api/dataset/{datasetName}/query?sortBy={attribute}&order={asc|desc}`

#### Request Example:
```bash
curl -X GET "http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc"
```

#### Response Example (`200 OK`):
```json
{
  "sortedRecords": [
    { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" },
    { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" },
    { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
  ]
}
```

---

## 🧪 Testing

Run automated unit and integration tests using:

```bash
# On Linux / macOS
./mvnw test

# On Windows
.\mvnw.cmd test
```

---

## 📂 Project Structure

```text
json-dataset-query-engine/
├── .mvn/wrapper/              # Maven Wrapper files
├── src/
│   ├── main/
│   │   ├── java/              # Spring Boot Application code
│   │   └── resources/
│   │       ├── db/migration/  # Flyway SQL migrations
│   │       └── application.yml# Application configuration
│   └── test/
│       └── java/              # Unit and integration test suites
├── pom.xml                    # Maven dependencies and build configuration
└── README.md                  # Project documentation
```


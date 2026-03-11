# GenesisWMS — Warehouse Management System API

> 13 REST endpoints across 3 domains · 10 integration tests · OpenAPI 3 documentation · RFC 7807 error handling

A production-ready RESTful API for warehouse management built with Spring Boot 3.5, PostgreSQL, and Java 21.

## Tech Stack

- **Java 21** — Records, modern language features
- **Spring Boot 3.5** — Web, Data JPA, Validation
- **PostgreSQL 16** — Primary database
- **Hibernate 6** — ORM with auto schema management
- **SpringDoc OpenAPI 3** — Auto-generated Swagger UI
- **H2 / Testcontainers** — Integration testing with in-memory database
- **Lombok** — Boilerplate reduction
- **Docker** — Local database setup

## Architecture

Layered architecture following separation of concerns:
```
controller/   → HTTP layer, request/response handling
service/      → Business logic
repository/   → Data access (Spring Data JPA)
entity/       → JPA domain models
dto/          → Request/Response transfer objects
exception/    → Global error handling (RFC 7807)
```

## API Endpoints

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a new product |
| GET | `/api/products` | List all products (filter by category) |
| GET | `/api/products/{sku}` | Get product by SKU |
| PUT | `/api/products/{sku}` | Update product |
| DELETE | `/api/products/{sku}` | Delete product |

### Inventory
| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/inventory/{sku}` | Set stock level |
| PATCH | `/api/inventory/{sku}/adjust` | Adjust stock by delta |
| GET | `/api/inventory/{sku}` | Get stock level |
| GET | `/api/inventory/low-stock` | Get low stock items |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders` | List all orders (filter by status) |
| GET | `/api/orders/{id}` | Get order by ID |
| PATCH | `/api/orders/{id}/status` | Update order status |

## Prerequisites

- Java 21
- Docker Desktop
- Maven 3.9+

## Quick Start

**1. Start the database:**
```bash
docker run --name genesis-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=genesis_wms \
  -p 5432:5432 -d postgres:16
```

**2. Run the application:**
```bash
./mvnw spring-boot:run
```

**3. Open API docs:**
```
http://localhost:8080/swagger-ui.html
```

## Running Tests
```bash
./mvnw test
```

Runs 10 integration tests covering Products and Inventory endpoints including edge cases (duplicate SKU, negative stock, 404 after delete).

## Error Handling

All errors follow RFC 7807 Problem Detail format:
```json
{
  "status": 409,
  "title": "Conflict",
  "detail": "Product with SKU 'SKU-001' already exists"
}
```

## Key Design Decisions

- **DTO pattern** — entities never exposed directly; all I/O mapped through request/response records
- **RFC 7807 Problem Detail** — standardized error responses across all endpoints
- **PATCH for partial updates** — `/adjust` and `/status` endpoints use PATCH correctly vs PUT for full replacement
- **@PrePersist / @PreUpdate** — audit timestamps handled automatically at the entity level
- **Layered architecture** — strict separation between HTTP, business logic, and data access layers
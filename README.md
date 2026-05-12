# Kolex Backend

REST API and server-side UI for **Kolex**, a train ticketing system used by a mobile client (Android) and an **admin web panel**. Users register, manage passenger profiles, search trips, hold seats, purchase tickets with an account balance, and manage refunds. PostgreSQL holds the schema; Flyway applies migrations on startup.

---

## Tech stack

| Area | Technology |
|------|------------|
| Runtime | Java **21** |
| Framework | Spring Boot **4.x** (Web MVC, Data JPA, Security, Thymeleaf) |
| Database | **PostgreSQL** |
| Migrations | **Flyway** |
| API docs | **springdoc-openapi** (Swagger UI) |
| Auth (API) | **JWT** (access + refresh tokens in responses) |
| Auth (admin) | Session-based **form login** with role **ADMIN** |
| Build | **Maven** |

---

## Prerequisites

- **JDK 21**
- **Maven 3.8+**
- **PostgreSQL** reachable from the machine running the app

Create an empty database (name can match your JDBC URL). On first run, Flyway creates and upgrades the schema from `src/main/resources/db/migration`.

---

## Configuration

Settings live in `src/main/resources/application.properties`. For anything beyond local development, **override secrets and URLs** via environment variables or a profile-specific config file—do **not** commit production passwords or JWT secrets.

Important keys:

| Property | Purpose |
|----------|---------|
| `spring.datasource.*` | JDBC URL, username, password |
| `server.port` | HTTP port (default **8080**) |
| `jwt.secret` | Signing key for JWT (must be strong and private in production) |
| `jwt.expiration` / `jwt.refresh-expiration` | Token lifetimes (milliseconds) |
| `kolex-urls` | Comma-separated **CORS allowed origin patterns** for the mobile/web clients |

Example datasource (adjust host, port, database name):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/kolexdb
spring.datasource.username=your_user
spring.datasource.password=your_password
```

---

## Run locally

1. Start PostgreSQL and ensure the database exists.
2. Copy or edit `application.properties` so JDBC settings match your DB.
3. From the project root:

```bash
./mvnw spring-boot:run
```

If you do not use the Maven wrapper:

```bash
mvn spring-boot:run
```

The application listens on **http://localhost:8080** (unless `server.port` is changed).

---

## API documentation (Swagger)

Interactive docs are enabled by default:

- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8080/api-docs  

Use Swagger to inspect request/response schemas and try authenticated routes after obtaining a JWT from `/api/auth/login` or `/api/auth/register`.

---

## Authentication (REST API)

1. **Register:** `POST /api/auth/register`  
2. **Login:** `POST /api/auth/login`  

Responses include `token`, `refreshToken`, `type`, `email`, and `userId` (`AuthResponse`).

For protected endpoints, send:

```http
Authorization: Bearer <access_token>
```

**Public API routes** (no JWT): `/api/auth/**`, `/api/metadata/**`. All other `/api/**` routes require a valid JWT.

---

## REST API overview

| Prefix | Role |
|--------|------|
| `/api/auth` | Register, login |
| `/api/metadata` | Reference data (e.g. stations) |
| `/api/profiles` | CRUD for passenger profiles (authenticated) |
| `/api/search` | Search trains, seats, stations |
| `/api/reservations` | Hold seat, cancel reservation |
| `/api/payment` | Buy ticket, random purchase, refund, wallet top-up |
| `/api/tickets` | List current user’s tickets |
| `/api/users` | Balance, account deactivation |

Exact paths and bodies are defined on the controllers under `src/main/java/net/f1v/kolexbackend/controller/` and in Swagger.

---

## Admin web UI

Non-`/api/**` traffic uses a **separate** security chain: form login, server-side sessions, and Thymeleaf templates.

- **Login page:** `/login`  
- **Dashboard and management:** `/admin/**` — requires authority **ADMIN**  

Static assets (`/css/**`, `/images/**`, …) and the login page are permitted without authentication.

---

## Background jobs

Expired seat holds are cleaned periodically (`ReservationCleanupTask`: scheduled cleanup of held reservations).

---

## Tests

```bash
./mvnw test
```

---

## Project layout (high level)

```
src/main/java/net/f1v/kolexbackend/
├── config/          # Security, JWT, OpenAPI, user details
├── controller/      # REST controllers + web login/admin
├── dto/             # Request/response models
├── entity/          # JPA entities
├── repository/      # Spring Data repositories
├── error/           # Exception controllers
├── service/         # Business logic
└── scheduled/       # Scheduled tasks

src/main/resources/
├── application.properties
├── db/migration/    # Flyway SQL migrations
├── static/          # CSS, images
└── templates/       # Thymeleaf (login, admin)
```

---

## Related repositories

This backend is intended to work together with the **Kolex mobile app** and any frontend that consumes `/api/**`. Point clients at the same origin patterns configured in `kolex-urls` so CORS allows the requests.

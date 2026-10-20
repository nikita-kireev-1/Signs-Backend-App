# Signs Backend App

Backend for a location-based signs/notes application.

The API allows users to register, authenticate, create signs tied to geographic coordinates, react to them, report them, and search for nearby signs.

## Tech Stack

- Kotlin
- Ktor
- PostgreSQL
- JetBrains Exposed
- Koin
- JWT
- BCrypt
- kotlinx.serialization
- Swagger / OpenAPI
- Gradle Kotlin DSL

## Features

- Registration and login
- JWT authentication
- User management
- Create, update, delete and search signs
- Search signs by author and location
- Like/dislike reactions
- Sign reports
- PostgreSQL persistence
- Centralized error handling
- Swagger/OpenAPI documentation
- API contract testing with Schemathesis

## Architecture

The project uses a simple layered structure:

```text
Route -> Handler -> Service -> Repository -> PostgreSQL
```

Main packages:

```text
src/main/kotlin/
├── config/
├── di/
├── exception/
├── handler/
├── model/
├── repository/
├── routing/
├── service/
├── sql/
└── Application.kt
```

## Running Locally

### Requirements

- JDK 26
- PostgreSQL
- Git

### Clone

```bash
git clone https://github.com/nikita-kireev-1/Signs-Backend-App.git
cd Signs-Backend-App
```

### Configure

Create a PostgreSQL database and make sure its name matches the database name in `db.url`.

Create `local.properties` in the project root:

```properties
ktor.deployment.port=8080
ktor.deployment.host=0.0.0.0

db.url=jdbc:postgresql://localhost:5432/mydb
db.user=postgres
db.password=your_password
db.driver=org.postgresql.Driver

app.env=dev

jwt.secret=replace_with_a_secure_secret
jwt.issuer=signs-server
jwt.audience=signs-client
jwt.expiresInMs=3600000
```

`local.properties` and `.env` are ignored by Git and should not contain committed secrets.

### Run

Linux/macOS:

```bash
./gradlew run
```

Windows:

```powershell
gradlew.bat run
```

The server runs on:

```text
http://127.0.0.1:8080
```

Health check:

```bash
curl http://127.0.0.1:8080/health
```

## API

Main API prefix:

```text
/api
```

Main route groups:

```text
/api/auth
/api/user
/api/sign
/api/signReaction
/api/signReport
```

Authentication is done with a Bearer token:

```http
Authorization: Bearer <jwt>
```

Swagger/OpenAPI:

```text
http://127.0.0.1:8080/swagger-ui
```

OpenAPI JSON:

```text
http://127.0.0.1:8080/swagger-ui/api.json
```

## Notes

All endpoints that require input data intentionally accept JSON request bodies instead of query parameters.

Location search currently uses a latitude/longitude bounding box rather than PostGIS or an exact geographic-radius calculation.

## Testing

The generated OpenAPI schema can be tested with Schemathesis:

```bash
uvx schemathesis run   http://127.0.0.1:8080/swagger-ui/api.json   --url http://127.0.0.1:8080
```

## Branches

- `master` — stable version
- `development` — active development

## Status

The project is under active development and is intended to be used as the backend for a future mobile application.

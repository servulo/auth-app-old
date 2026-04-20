# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Quarkus 3.33.1** (Java 25) backend for a JWT-authenticated REST API. The backend uses Hibernate ORM with Panache (active record pattern), SmallRye JWT for authentication, and PostgreSQL as the database.

## Commands

All Maven commands run from `auth-backend/` using the Maven wrapper:

```bash
# Dev mode with hot reload (Dev UI at http://localhost:8080/q/dev/)
./mvnw quarkus:dev

# Build
./mvnw package

# Run tests
./mvnw test

# Run integration tests
./mvnw verify

# Native image build
./mvnw package -Dnative

# Native image via Docker (no local GraalVM needed)
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Architecture

**Stack:**
- REST: RESTEasy (JAX-RS) with Jackson for JSON
- ORM: Hibernate Panache (active record pattern — entities extend `PanacheEntity`)
- Auth: SmallRye JWT (MicroProfile JWT)
- DB: PostgreSQL
- DI: Quarkus Arc (CDI)

**Expected package layout** (under `src/main/java/`):
- `resource/` — JAX-RS endpoints (`@Path`, `@GET`, `@POST`, etc.)
- `entity/` — Panache entities (extend `PanacheEntity` or `PanacheEntityBase`)
- `service/` — Business logic (`@ApplicationScoped` beans)
- `security/` — JWT generation/validation helpers

**Runtime config** goes in `src/main/resources/application.properties`. Key properties to configure: `quarkus.datasource.*`, `mp.jwt.verify.publickey*`, `smallrye.jwt.sign.key*`.

## Deployment

Four Dockerfile variants are available in `src/main/docker/`:
- `Dockerfile.jvm` — standard JVM (UBI9 + OpenJDK 25)
- `Dockerfile.native` / `Dockerfile.native-micro` — GraalVM native executable
- `Dockerfile.legacy-jar` — fat JAR format

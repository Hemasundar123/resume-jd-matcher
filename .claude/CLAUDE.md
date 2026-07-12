# Project: AI Resume↔JD Matcher

## Overview
Spring Boot backend that scores a resume against a job description using
semantic embeddings (pgvector) and Claude-generated qualitative feedback.

## Tech & Conventions
- Java 21, Spring Boot 3.4, Spring AI 1.0
- Package root: com.hemasundar.matcher
- Layered architecture: controller → service → repository
- DTOs for all API I/O; never expose JPA entities directly
- Use constructor injection (no @Autowired on fields)
- Persistence: PostgreSQL + pgvector; migrations via Flyway
- Prefer records for DTOs and immutable data
- Validation via jakarta.validation annotations

## Build & Run
- Build: `./mvnw clean package`
- Run: `./mvnw spring-boot:run`
- Infra: `docker compose up -d`

## Coding rules for Claude
- Keep services single-responsibility.
- Write meaningful Javadoc on public service methods.
- Add unit tests for service logic (JUnit 5 + Mockito).
- No secrets in code; read from env vars.
- Explain non-obvious design choices in comments.
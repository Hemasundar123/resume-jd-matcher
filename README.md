# 🤖 AI-Powered Resume ↔ JD Matcher

Match a resume against a job description using Spring Boot + Spring AI,
with vector embeddings for semantic scoring and AI-generated qualitative
feedback. Fully deployed: Firebase (frontend + auth), Railway (backend),
Neon (Postgres + pgvector).

> Built with Claude Code as an AI pair-programmer. Architecture and every
> component are understood and owned by the author.

**Live app:** https://resume-ats-61056.web.app

## What it does
1. Sign in with Google (Firebase Authentication).
2. Submit a resume (pasted text or PDF upload) and a job description.
3. Backend extracts text, generates embeddings for both documents.
4. Computes semantic similarity (cosine similarity via pgvector).
5. Calls an LLM for qualitative feedback: matching skills, gaps, suggestions.
6. Returns a structured match report, persisted and viewable in the UI.

## Architecture

| Layer | Technology | Hosted on |
|---|---|---|
| Frontend | React + Vite | Firebase Hosting |
| Auth | Firebase Authentication (Google sign-in) | Firebase |
| Backend | Java 21, Spring Boot 3.4, Spring AI 1.0 | Railway (Docker) |
| Database | PostgreSQL + pgvector | Neon (serverless Postgres) |
| Embeddings | Local ONNX model (`all-MiniLM-L6-v2`, 384-dim) via Spring AI Transformers | runs in-process on Railway |
| Chat / feedback model | OpenAI-compatible endpoint via [NaraRouter](https://router.bynara.id) | external API |
| Migrations | Flyway | applied on boot |

Firebase does not run JVM backends or Postgres directly — Cloud Run/Railway
and Neon fill those roles under the same overall project, while Firebase
handles what it's actually built for: static hosting and auth.

## Tech Stack
- Java 21, Spring Boot 3.4, Spring AI 1.0
- PostgreSQL 16+ with pgvector extension (hosted on Neon)
- Apache PDFBox (PDF text extraction)
- Docker (multi-stage build for deployment)
- React 18 + Vite, Firebase SDK (Auth + Hosting)
- Maven

## Local Development

Requires: Java 21, Node 18+, Docker (optional, for local Postgres), a NaraRouter API key.

```bash
# backend
export SPRING_DATASOURCE_URL="jdbc:postgresql://<your-neon-host>/neondb?sslmode=require"
export SPRING_DATASOURCE_PASSWORD="<your-neon-password>"
export SPRING_AI_OPENAI_API_KEY="<your-nararouter-key>"

cd backend
./mvnw spring-boot:run

# frontend (separate terminal)
cd frontend
npm install
npm run dev
```

Backend: http://localhost:8080 · Frontend: http://localhost:5173

### Running the backend in Docker instead

```bash
docker compose build backend
docker compose up -d
```

`docker-compose.yml` expects `NEON_DB_PASSWORD` and `NARAROUTER_API_KEY` to be
set in your shell before running — see `docker-compose.yml` for the exact
environment variable names it forwards into the container.

## API (v1)

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/v1/match | resume + JD text → match report |
| POST | /api/v1/match/upload | resume PDF + JD text (multipart) → match report |
| GET  | /api/v1/match/{id} | retrieve a stored report |
| GET  | /actuator/health | health check |

All `/api/**` routes require a valid Firebase ID token in requests from the
deployed frontend; CORS is restricted to the deployed origin and
`localhost:5173` for local dev.

## Deployment

- **Frontend**: `cd frontend && npm run build && firebase deploy --only hosting`
- **Backend**: `cd backend && railway up` (Railway builds from `backend/Dockerfile`)
- **Database**: Neon project, `CREATE EXTENSION vector;` run once via Neon's SQL Editor;
  Flyway applies the rest (`documents`, `match_reports` tables) automatically on backend boot.

Environment variables the backend needs in production (set via `railway variables`):
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`,
`SPRING_AI_OPENAI_API_KEY`, `SPRING_AI_OPENAI_BASE_URL`.

## Project Status

| Step | What | Status |
|---|---|---|
| 1–4 | Scaffold, schema, embeddings, match orchestration | ✅ |
| 5 | REST API | ✅ |
| 6 | React frontend | ✅ |
| 7 | Dockerized backend | ✅ |
| 8 | Firebase Auth + Hosting | ✅ |
| 9 | Neon (Postgres + pgvector) | ✅ |
| 10 | Railway backend deployment | ✅ |
| — | Tests, README polish, demo | ⏳ |

Author: Jagilinki Hemasundar · B.Tech CSE

# 🤖 AI-Powered Resume ↔ JD Matcher

Match a resume against a job description using Spring Boot + Spring AI (Claude)
with vector embeddings for semantic scoring.

> Built with Claude Code as an AI pair-programmer. Architecture and every
> component are understood and owned by the author.

## What it does
1. Upload a resume (PDF/text) and a job description.
2. Extract text, generate embeddings for both.
3. Compute semantic similarity (cosine similarity via pgvector).
4. Call Claude for qualitative feedback: matching skills, gaps, suggestions.
5. Return a structured match report via REST API.

## Tech Stack
- Java 21, Spring Boot 3.4
- Spring AI 1.0 (Anthropic Claude + embeddings)
- PostgreSQL 16 + pgvector
- Apache PDFBox, Docker, React + Vite, Maven

## Quick Start
\`\`\`bash
export ANTHROPIC_API_KEY=sk-ant-...
docker compose up -d          # Postgres + pgvector
cd backend && ./mvnw spring-boot:run
cd frontend && npm install && npm run dev
\`\`\`

API: http://localhost:8080 · Frontend: http://localhost:5173

## API (v1)
| Method | Path | Description |
|--------|------|-------------|
| POST | /api/v1/match | resume + JD text → match report |
| POST | /api/v1/match/upload | resume PDF + JD text (multipart) |
| GET  | /api/v1/match/{id} | retrieve stored report |
| GET  | /actuator/health | health check |
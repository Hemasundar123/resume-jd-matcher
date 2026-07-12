# Architecture

## Flow
1. Client submits resume text + JD text (or uploads a PDF).
2. DocumentService extracts clean plain text.
3. EmbeddingService generates a vector for each document.
4. MatchService computes cosine similarity between the two vectors.
5. FeedbackService asks Claude for matching skills, gaps, and suggestions.
6. MatchService assembles a MatchReport, persists it, and returns a DTO.

## Why these choices
- **pgvector**: keeps embeddings + relational data in one DB; no extra service.
- **Spring AI**: idiomatic Java access to Claude + embeddings; swappable models.
- **Layered design**: controller/service/repository keeps concerns isolated
  and unit-testable.
- **Flyway**: versioned, reproducible schema (owns the vector table too).
- **DTOs (records)**: never leak JPA entities across the API boundary.

## Modules (build order)
1. Domain model + schema (Step 2)
2. Document parsing + embeddings (Step 3)
3. Claude feedback + orchestration (Step 4)
4. REST layer (Step 5)
5. Frontend (Step 6)
6. Docker/deploy (Step 7)
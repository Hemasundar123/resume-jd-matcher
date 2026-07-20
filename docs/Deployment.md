# Deployment

## Local (docker compose)

```bash
docker compose up -d --build
```

Backend: http://localhost:8080 · Postgres: localhost:5432
Ollama must be running natively on the host (`ollama serve`) — it isn't containerized here because
model weights are large and GPU passthrough into Docker adds complexity not worth it for local dev.

## Production paths

Two realistic options for this stack (Spring Boot + Postgres/pgvector + a chat model):

### Option A — Traditional VM / ECS / Kubernetes
- Push the backend image to a registry (ECR, GHCR, Docker Hub).
- Run Postgres as a managed service (RDS for Postgres supports pgvector as of recent versions — confirm
  the extension is enabled on your instance/version before relying on it).
- Chat model: either point at a hosted Claude/Anthropic API (swap the `ai.ollama` block back to
  `ai.anthropic` in `application.yml` — see Step 4) or run Ollama on a GPU-backed instance if you want
  to stay fully local-model.
- Standard container orchestration from there: ECS task definition or a Kubernetes Deployment +
  Service + Ingress, secrets injected as env vars, not baked into the image.

### Option B — Google Cloud Run + Cloud SQL (recommended if pairing with Firebase)
- Cloud Run runs the Dockerized backend directly — no Kubernetes to manage, scales to zero.
- Cloud SQL for Postgres hosts the database; pgvector is supported as of recent Cloud SQL Postgres
  versions — verify the specific version you provision supports the extension before migrating.
- This path is the one that makes sense once you add Firebase Auth + Firebase Hosting (next phase),
  since Cloud Run and Firebase share the same GCP project and IAM — this is covered in full detail
  once we get there.

Either way: never bake `ANTHROPIC_API_KEY` or DB credentials into the image. Inject via the platform's
secret manager (AWS Secrets Manager, GCP Secret Manager, or plain env vars for a first pass).
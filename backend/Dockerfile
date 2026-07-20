# ---------- Stage 1: build ----------
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy wrapper + pom first so dependency layer caches independently of source changes
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw clean package -DskipTests -B

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Non-root user — don't run the JVM as root in production
RUN groupadd -r matcher && useradd -r -g matcher matcher
COPY --from=build /app/target/*.jar app.jar
RUN chown matcher:matcher app.jar
USER matcher

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
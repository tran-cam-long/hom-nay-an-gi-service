FROM gradle:8.11.1-jdk17 AS builder
WORKDIR /workspace

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies || true

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN useradd -r -u 10001 spring
USER spring

COPY --from=builder /workspace/build/libs/*jar /app/app.jar

EXPOSE 8100

ENV SERVER_PORT=8100
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
FROM gradle:7.6.1-jdk17 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the jar from build stage
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
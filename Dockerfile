FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/statsbot.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
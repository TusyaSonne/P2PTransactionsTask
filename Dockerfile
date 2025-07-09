# Этап 1 — сборка jar
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Этап 2 — запуск jar
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/P2PTransactionsTask-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
# Multi-stage build for Spring Boot application
FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/finance-backend-*.jar app.jar

# Oracle JDBC driver (if needed in container)
RUN apk add --no-cache curl

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--server.port=8080"]

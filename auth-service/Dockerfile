# Stage 1: build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn -pl auth-service -am clean package -DskipTests

# Stage 2: run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/auth-service/target/auth-service-*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]

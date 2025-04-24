# stage 1: build
# -------------------------------
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -pl user-service -am -DskipTests

# stage 2: run
# -------------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=build /app/user-service/target/user-service-*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]

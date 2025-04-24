# stage 1: build
# -------------------------------
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -pl music-catalog-service -am -DskipTests

# stage 2: run
# -------------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=build /app/music-catalog-service/target/music-catalog-service-*.jar app.jar

EXPOSE 8093

ENTRYPOINT ["java", "-jar", "app.jar"]

# stage 1: build
# -------------------------------
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -pl tour-service -am -DskipTests

# stage 2: run
# -------------------------------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=build /app/tour-service/target/tour-service-*.jar app.jar

EXPOSE 8094

ENTRYPOINT ["java", "-jar", "app.jar"]

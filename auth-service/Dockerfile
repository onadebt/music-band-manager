FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY auth-service/target/auth-service-*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
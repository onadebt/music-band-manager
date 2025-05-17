FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY band-service/target/band-service-*.jar app.jar

EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]

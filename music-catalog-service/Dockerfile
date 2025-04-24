FROM docker.io/eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY ./music-catalog-service/target/music-catalog-service-*.jar app.jar

EXPOSE 8093

ENTRYPOINT ["java", "-jar", "app.jar"]
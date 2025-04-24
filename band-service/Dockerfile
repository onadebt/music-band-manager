FROM docker.io/eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY ./band-service/target/band-service-*.jar app.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "app.jar"]
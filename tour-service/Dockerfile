FROM docker.io/eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY ./tour-service/target/tour-service-*.jar app.jar

EXPOSE 8094

ENTRYPOINT ["java", "-jar", "app.jar"]
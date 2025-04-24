FROM docker.io/eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY ./user-service/target/user-service-*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
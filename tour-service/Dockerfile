FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY tour-service/target/tour-service-*.jar app.jar

EXPOSE 8094
ENTRYPOINT ["java", "-jar", "app.jar"]

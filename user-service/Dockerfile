FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY user-service/target/user-service-*.jar app.jar

EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]
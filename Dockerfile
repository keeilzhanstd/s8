FROM maven:3.9.0-eclipse-temurin-17 as build
COPY . /app
WORKDIR /app
RUN mvn package -Dmaven.test.skip


FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]
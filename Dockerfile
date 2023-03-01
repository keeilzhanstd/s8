#FROM maven:3.9.0-eclipse-temurin-17 as build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package
#
#FROM openjdk:17
#COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM openjdk:17
COPY target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

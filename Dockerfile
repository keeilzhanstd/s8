#FROM maven:3.9.0-eclipse-temurin-17 as build
#COPY . /app
#WORKDIR /app
#RUN mvn clean package

FROM maven:3.9.0-eclipse-temurin-17 AS build
COPY . /home/build
RUN mkdir /home/.m2
WORKDIR /home/.m2
USER root
RUN --mount=type=cache,target=/root/.m2 mvn -f /home/build/pom.xml clean package

FROM openjdk:17
WORKDIR /home/build
COPY --from=build /target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

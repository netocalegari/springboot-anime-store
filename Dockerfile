FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
COPY --from=build /target/animes_store-0.0.1-SNAPSHOT.jar anime_store_api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "dslist.jar"]

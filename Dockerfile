FROM maven:3.8.7-openjdk-18-slim AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/timmotors-0.0.1-SNAPSHOT.jar timmotors.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "timmotors.jar"]
# Stage 1: Build the application
FROM maven:4.0.0-openjdk-18-slim AS build
WORKDIR /app
COPY pom.xml . 
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:18-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/student-events-api-0.0.1-SNAPSHOT.jar ./app-UniEventos.jar
EXPOSE 8080
CMD ["java", "-jar", "app-UniEventos.jar"]

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/statuscode-0.0.1-SNAPSHOT.jar statuscode.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "statuscode.jar"]

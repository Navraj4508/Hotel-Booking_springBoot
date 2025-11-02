
# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /usr/app/
COPY --from=builder target/Hotel-Booking-0.0.1-SNAPSHOT.jar /usr/app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

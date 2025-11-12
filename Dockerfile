# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy source
COPY src src

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew clean build -x test

# Runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built jar from previous stage (adjust jar name if differs)
COPY --from=build /app/build/libs/psp-service-*.jar app.jar

# If the service listens on a port, expose it (example)
EXPOSE 8080

# Set default command
ENTRYPOINT ["java", "-jar", "app.jar"]

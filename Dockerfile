# --- Build stage ---
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar
CMD ["java", "-jar", "app.jar"]

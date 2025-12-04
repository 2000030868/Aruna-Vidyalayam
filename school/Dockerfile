# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

# Use maven wrapper or mvn if present
RUN chmod +x mvnw || true
RUN if [ -f mvnw ]; then ./mvnw clean package -DskipTests; else mvn clean package -DskipTests; fi

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# Use Gradle build image or JDK 21
FROM eclipse-temurin:21-jdk


WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Start: wait for MySQL, then run the app
ENTRYPOINT ["/wait-for-it.sh", "java", "-jar", "app.jar"]

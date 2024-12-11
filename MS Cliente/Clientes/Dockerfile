FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 5003
ENTRYPOINT ["java", "-jar", "app.jar"]
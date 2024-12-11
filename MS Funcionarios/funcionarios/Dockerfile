FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 5007
ENTRYPOINT ["java", "-jar", "app.jar"]
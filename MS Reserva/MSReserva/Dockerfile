FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
# porta usada para a chamada dos endpoints, por padrão o AUTH usa a 5000, usar a partir dai
EXPOSE 5005
ENTRYPOINT ["java", "-jar", "app.jar"]
# Etapa 1: Usar uma imagem base para construir a aplicação
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar todo o código do projeto para o container
COPY . .

# Construir o projeto (gera o arquivo .jar em target/)
RUN mvn clean package -DskipTests

# Etapa 2: Criar a imagem final apenas com o .jar
FROM eclipse-temurin:17-jre

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo .jar gerado na etapa anterior para o container
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 5001

# Comando para rodar a aplicação
# ENTRYPOINT ["java", "-jar", "app.jar"]
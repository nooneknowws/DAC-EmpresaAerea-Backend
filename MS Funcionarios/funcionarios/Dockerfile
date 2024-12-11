# Etapa 1: Usar uma imagem base para construir a aplicação
FROM gradle:8.4-jdk17 AS builder

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar apenas os arquivos necessários para instalar as dependências primeiro
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Baixar as dependências para cache
RUN gradle dependencies --no-daemon

# Copiar o restante do código-fonte
COPY . .

# Construir o projeto (gera o arquivo .jar em build/libs/)
RUN gradle bootJar --no-daemon

# Etapa 2: Criar a imagem final apenas com o .jar
FROM eclipse-temurin:17-jre

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo .jar gerado na etapa anterior para o container
COPY --from=builder /app/build/libs/*.jar app.jar


EXPOSE 5007

# Comando para rodar a aplicação
# ENTRYPOINT ["java", "-jar", "app.jar"]
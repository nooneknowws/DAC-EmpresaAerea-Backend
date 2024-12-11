#!/bin/bash

### Bancos
instala_rabbit() {
  echo "Instalando RabbitMQ..."
  docker pull rabbitmq:management
  docker run --name my-rabbitmq -p 15672:15672 -p 5672:5672 -d rabbitmq:management
    echo "RabbitMQ instalado com sucesso!"
}

instala_mongo() {
  echo "Instalando MongoDB..."
  docker pull mongo:latest
  docker run --name my-mongo -p 27017:27017 -d mongo
  echo "MongoDB instalado com sucesso!"
}

instala_postgres() {
  echo "Instalando PostgreSQL..."
  docker pull postgres:latest
  docker run --name my-postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
  echo "PostgreSQL instalado com sucesso!"
}


instalar_bancos() {
  instala_rabbit
  instala_mongo
  #instala_postgres
}

### Inicializa os bancos no docker
subir_bancos() {
  echo "Subindo containers dos bancos..."
  docker start my-rabbitmq my-mongo dac-postgres dac-pgadmin 2>/dev/null
#   docker start my-rabbitmq my-mongo my-postgres 2>/dev/null
  echo "Todos os bancos foram iniciados."
}

subir_api() {
  echo "Subindo API..."
  cd ./Gateway/
  node gateway.js &
  echo "API iniciada"
}


### DOCKER SCRIPTS
gerar_docker() {
  # SERVICE_NAME = $1
  # IMAGE_VERSION = $2
  # IMAGE_NAME="$SERVICE_NAME:$IMAGE_VERSION"

  IMAGE_NAME = $1

  echo "Buildando a imagem $IMAGE_NAME..."

  docker build -t $IMAGE_NAME .

  if [$? -eq 0]; then
    echo "$IMAGE_NAME foi buildado com sucesso"
  else
    echo "Ocorreu um erro ao criar a imagem $IMAGE_NAME"
    exit 1
  fi

  #docker images | grep $SERVICE_NAME => listar as imagens
  
}

buildar_auth() {
  cd ./MS\ auth/Auth
  gerar_docker ms-auth
  cd ..
  cd ..
}

buildar_cliente() {
  cd ./MS\ Cliente/Clientes
  gerar_docker ms-cliente
  cd ..
  cd ..
}

buildar_func() {
  cd ./MS\ Funcionarios/funcionarios
  gerar_docker ms-funcionarios
  cd ..
}

buildar_reserva() {
  cd ./MS\ Reserva
  gerar_docker ms-reserva
  cd ..
}

buildar_voos() {
  cd ./MS\ voos
  gerar_docker ms-voos
  cd ..
}

buildar_saga() {
  cd ./SAGA
  gerar_docker saga
  cd ..
}


gerar_imagens() {
  buildar_auth
  # buildar_cliente
  # buildar_func
  # buildar_reserva
  # buildar_voos
  # buildar_saga
}

#instalar_bancos
# subir_bancos
# subir_api
gerar_imagens
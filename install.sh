#!/bin/bash

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
  instala_postgres
}

instalar_bancos
#!/bin/bash

subir_bancos() {
  echo "Subindo containers dos bancos..."
  # docker start my-rabbitmq my-mongo dac-postgres dac-pgadmin 2>/dev/null
  docker start my-rabbitmq my-mongo my-postgres 2>/dev/null
  echo "Todos os bancos foram iniciados."
}

subir_api() {
  echo "Subindo API..."
  cd ./Gateway/
  node gateway.js
  echo "API iniciada"
}

gerar_imagens() {
  # AINDA A SER FINALIZADO
  docker compose up
}

gerar_imagens
subir_bancos
subir_api

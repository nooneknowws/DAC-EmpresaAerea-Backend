cadastrar_aeroporto() {
  API_URL="http://localhost:3000/api/aeroportos/"

  JSON_PAYLOAD='{
    "codigo": "POA",
    "nome": "Aeroporto Internacional Salgado Filho",
    "cidade": "Porto Alegre",
    "estado": "RS",
    "pais": "Brasil"
  }'

  HEADERS=(
    "-H" "Content-Type: application/json"
  )

  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "${HEADERS[@]}" -d "$JSON_PAYLOAD" "$API_URL")

  if [ "$RESPONSE" -eq 200 ]; then
    echo "Requisição enviada com sucesso!"
  else
    echo "Falha ao enviar requisição. Código HTTP: $RESPONSE"
  fi
}

cadastrar_aeroporto
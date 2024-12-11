# Etapa 1: Usar uma imagem base do Node.js
FROM node:18 AS builder

# Definir o diretório de trabalho no container
WORKDIR /app

# Instalar as dependências
RUN npm install

# Copiar o restante do código para o container
COPY . .

# Expor a porta padrão da API (se for 3000, ajuste conforme necessário)
EXPOSE 3000

# Comando para rodar a aplicação
CMD ["npm", "start"]

# DAC-EmpresaAerea-Backend

## TODO

Mensageria/SAGA
Implementar no front
Ajustar error codes
Limpar código

## Clonar o respositório.

`git clone + url`

  
  
  

## USO DO DOCKER - IMPORTANTE

### Criar um dockerfile  dentro da pasta do PROJETO do microserviço ex:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
# porta usada para a chamada dos endpoints, por padrão o AUTH usa a 5000, usar a partir dai
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]
```
### Abrir um console dentro da pasta do projeto e executar os seguintes comandos em ordem
```bash 
./gradlew build
```

```bash
docker build -t nome-da-imagem .
```
```bash
docker run -d -p 500X:500X nome-da-imagem
```
### Instalação do postgres/rabbitMQ no docker - IMPORTANTISSIMO

#### RabbitMQ
baixar a imagem
```bash
docker pull rabbitmq:management
```
criar o container
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```
Após instalar e rodar no docker da pra entrar na porta do rabbitMQ pra ter um "pgadmin" dele
http://localhost:15672
Usuario: guest
Senha: guest

#### PostgreSQL
baixar as imagens
```bash
docker pull postgres
docker pull dpage/pgadmin4
```
criar a network
```bash
docker network create --driver bridge postgres-network
```
criar o container do PostgreSQL
```bash
docker run --name dac-postgres --network=postgres-network -e "POSTGRES_PASSWORD=postgres" -p 5432:5432 -v /users/postgres/data -d postgres
lembrar que caso você ja tenha o PostgreSQL instalado no PC é bom trocar a porta de origem pra 5433, ficando 5433:5432 pra evitar conflitos
```
criar o container do PGAdmin4
```bash
docker run --name pgadmin --network=postgres-network -e "PGADMIN_DEFAULT_EMAIL=admin@admin.com" -e "PGADMIN_DEFAULT_PASSWORD=postgres" -p 15432:80 -d dpage/pgadmin4
```




//colocar o numero e link das versões
+ Versão 20.17.0 do [Node](). 
+ Versão 10.8.2 do [npm]().
+ Versão 17 do [Java](). 
+ Versão 4.4.26.0 do [Springboot]().
+ Versão 18.2.4 do [Angular]().
+ Versão 5.6.3 do [TS](). 

Após isso você deve conferir se estão adicionadas ao PATH nas variaveis de ambiente.

1. Em variaveis de sistema adicione **JAVA_HOME** com o caminho para a sua versão do java e **ANDROID_HOME**.
2. Depois adicione à Path o bin do seu java **%JAVA_HOME%\bin**.
3. Demais configurações....

## Após clonar e configurar tudo você irá installar as libs utilizando o comando:

1. `npm install`
2. Demais comandos...

## Requisitos Mínimos para Entrega e Defesa
- **Front-end**:
  - Implementado em **Angular + TypeScript**
  - Interface visual bem elaborada
  - **Acessa o API Gateway** via **HTTP-REST**
  - **Não** utiliza **Local Storage** ou **json-server** para armazenamento

- **Back-end**:
  - Implementado em **Spring Boot** (Java ou Kotlin)
  - Arquitetura baseada em **microsserviços**
  - Cada microsserviço usa um banco de dados distinto
  - Login e cadastros básicos funcionando completamente
  - Implementação completa de uma **SAGA** para gerenciamento de transações distribuídas
  - **Mensageria** via **RabbitMQ**

- **API Gateway**:
  - Implementado um **API Gateway básico** para comunicação entre frontend e backend

- **Outros requisitos**:
  - Interface de usuário com design refinado (não pode ser HTML puro ou interface mal projetada)
 
# Requisitos Funcionais

O objetivo deste trabalho é o desenvolvimento de um sistema de Gestão de Empresa Aérea usando **Angular** e **Java Spring**, baseado na arquitetura de microsserviços.

## Perfis de Acesso

- **Cliente**: Usuários com este perfil são os clientes da agência.
- **Funcionário**: Usuários com este perfil são os funcionários da agência.

### Requisitos Funcionais

- **R01: Autocadastro**
  - Um cliente não cadastrado pode se cadastrar, informando:
    - CPF, Nome, E-mail, Rua/Número, Complemento, CEP, Cidade e Estado.
    - O cliente inicia com 0 milhas.
    - Senha gerada aleatoriamente (número de 4 dígitos) e enviada por e-mail.
  
- **R02: Efetuar Login/Logout**
  - Login com e-mail/senha.
  - Acesso às funcionalidades do sistema após login bem-sucedido.

### Perfil Cliente

- **R03: Mostrar Tela Inicial de Cliente**
  - Menu com operações disponíveis e saldo de milhas.
  - Tabela com reservas, voos realizados e cancelados, mostrando data/hora, Aeroporto Origem/Destino.
  - Opções de ver reserva (R04) e cancelar reserva (R08).

- **R04: Ver Reserva**
  - Exibe os dados da reserva: data/hora, código, origem, destino, valor gasto, milhas gastas e estado da reserva.

- **R05: Comprar Milhas**
  - Proporção de 1 milha por R$ 5,00.
  - Registro de transações de compra de milhas: data/hora, valor em reais, quantidade de milhas, descrição "COMPRA DE MILHAS".

- **R06: Consultar Extrato de Milhas**
  - Tabela com quantidade de milhas e histórico de transações (compra e uso em voos).

- **R07: Efetuar Reserva**
  - Buscar voos com origem/destino e selecionar voo desejado.
  - Exibir dados do voo e permitir compra de passagens usando milhas e dinheiro.

- **R08: Cancelar Reserva**
  - Cancela a reserva e retorna milhas ao saldo do cliente.
  - Registro de milhas de cancelamento e data/hora do cancelamento.

- **R09: Consultar Reserva**
  - Consulta reserva pelo código e exibe todos os dados do voo. Oferece opções de check-in ou cancelamento se aplicável.

- **R10: Fazer Check-in**
  - Exibe voos nas próximas 48h para o cliente fazer check-in.

### Perfil Funcionário

- **R11: Tela Inicial do Funcionário**
  - Tabela de voos das próximas 48h, com opções para confirmar embarque (R12), cancelar voo (R13) ou registrar voo realizado (R14).

- **R12: Confirmação de Embarque**
  - Digitação do código de reserva para confirmar embarque.

- **R13: Cancelamento de Voo**
  - Cancela voo e todas as reservas associadas, alterando o estado para CANCELADO.

- **R14: Realização do Voo**
  - Registra que o voo ocorreu e altera o estado das reservas para REALIZADO ou NÃO REALIZADO.

- **R15: Cadastro de Voo**
  - Permite cadastrar voos com dados como código, data/hora, origem, destino, valor da passagem e quantidade de poltronas.

- **R16: Listagem de Funcionários (CRUD)**
  - Exibe lista de funcionários com Nome, CPF, E-mail e Telefone.

- **R17: Inserção de Funcionário (CRUD)**
  - Inserção de funcionário com senha aleatória de 4 dígitos enviada por e-mail.

- **R18: Alteração de Funcionário (CRUD)**
  - Permite a alteração dos dados do funcionário, exceto CPF.

- **R19: Remoção de Funcionário (CRUD)**
  - Inativa o funcionário em vez de apagar os dados.

## Decomposição Por Subdomínio

### Autenticação
- Responsável pela autenticação no sistema.
- Tabela para dados de usuário: login, senha, Tipo (cliente/funcionário).

### Cliente
- Responsável pela manutenção dos clientes.
- Tabela de dados de cliente: CPF, Nome, E-mail, Endereço, Milhas.
- Tabela de transações de milhas: data/hora, quantidade de milhas, entrada/saída, descrição.

### Voos
- Responsável pela manutenção dos voos.
- Tabela de Aeroportos: Código, Nome, Cidade, Estado.
- Tabela de Voos: Código, Data/hora, Aeroporto Origem/Destino, valor da passagem, poltronas.

### Reservas
- Responsável pelas reservas.
- Tabela de Reservas: Código, Voo, Data/hora, Estado.
- Tabela de Histórico de Alteração de Estado de Reserva.

### Funcionário
- Responsável pelos dados de funcionários.
- Tabela de Funcionários: Nome, CPF, E-mail, Telefone.

## Arquitetura

O sistema deve seguir os seguintes padrões de projeto de microsserviços:

- **Arquitetura de Microsserviços**: para desenvolver o software.
- **Padrão API Gateway**: para expor a API.
- **Padrão Database per Service**: cada serviço possui seu próprio banco de dados.
- **Padrão CQRS**: no microsserviço de Reserva, para todas as consultas.
- **Padrão SAGA Orquestrada**: para transações que envolvem vários serviços.
- **Padrão API Composition**: para agregar resultados de consultas de múltiplos serviços.

### Docker
Cada microsserviço, incluindo o API Gateway e bancos de dados, deve ser executado em uma imagem Docker separada.

# Requisitos Não-Funcionais e Comentários

## 1. Documentação de Suposições
- **Qualquer suposição feita pela equipe que não esteja definida no escopo deve ser documentada e entregue em formato PDF, junto ao projeto final.

## 2. Tecnologias a Serem Utilizadas
- **Front-end:** Angular 13 ou superior.
- **Back-end:** Node.js, Spring Boot (Java ou Kotlin), Spring Data JPA.
- **Bancos de Dados:** PostgreSQL e MongoDB.
- **Containerização:** Docker para cada microsserviço e banco de dados.
- **Mensageria:** RabbitMQ para comunicação assíncrona entre microsserviços.
- **Pesquisa:** Implementação de RabbitMQ para a mensageria e SAGA Orquestrada (necessário aprendizado autônomo).
- **API Gateway:** Para comunicação entre front-end e microsserviços.

## 3. Restrições
- **Proibição de Geradores de Código:** O uso de geradores automáticos de código não é permitido, incentivando o desenvolvimento manual.
- **Independência dos Microsserviços:** Cada microsserviço deve ter seu próprio banco de dados e não pode acessar o banco de dados de outro serviço.
  - **Seguir o padrão *Database Per Service*.
- **MongoDB para Autenticação:** O microsserviço de autenticação deve utilizar o MongoDB, enquanto os outros microsserviços utilizarão PostgreSQL.

## 4. Padrões Arquiteturais
- **Arquitetura de Microsserviços:** Os serviços devem ser independentes e especializados.
- **API Gateway:** O front-end Angular se comunica apenas com o API Gateway, que distribui as requisições aos microsserviços.
- **Mensageria Assíncrona (RabbitMQ):** Comunicação assíncrona entre microsserviços via RabbitMQ.
- **SAGA Orquestrada:** Utilização do padrão SAGA para transações distribuídas.

## 5. Modelagem e Maturidade
- **Modelo de Maturidade Richardson (Nível 2):** Implementar HATEOAS no nível 2 para navegação entre recursos via links.

## 6. Containerização e Deploy
- **Cada microsserviço, banco de dados e o API Gateway devem ser empacotados em containers Docker. 
  - **A geração e execução das imagens deve ser automatizada via scripts de shell.

## 7. CQRS no Microsserviço de Reserva
- **CQRS (Command Query Responsibility Segregation):** Implementar CQRS no microsserviço de Reservas.
  - **A base de comando deve ser normalizada e a base de consulta pode ser desnormalizada.
  - **Comunicação assíncrona via RabbitMQ para a atualização da base de consulta.

## 8. Segurança e Criptografia
- **Criptografia de Senhas:** Usar o algoritmo SHA-256 com SALT para criptografar as senhas.

## 9. Validação e Formatação
- **Validação de Campos:** Todos os campos que necessitarem de validação devem ser implementados no front-end.
- **Formatação Brasileira:** Datas e valores monetários devem seguir o padrão brasileiro (ex.: `dd/mm/aaaa` e `R$ xxx,xx`).
- **Máscara de Entrada:** Campos com formatação (ex.: CPF, CEP, telefone) devem ter máscaras de entrada.

## 10. Interface e Usabilidade
- **Front-end:** A interface deve ser construída com Angular e usar Bootstrap, Tailwind ou Material.
- **Compatibilidade:** O sistema será testado no **Firefox**, versão mais recente.

## 11. Execução e Build Automatizado
- **O processo de build, geração de imagens Docker e execução deve ser automatizado com um shell script.

---

## Comentários Adicionais
- **Padrões Arquiteturais:** A arquitetura de microsserviços oferece escalabilidade, mas aumenta a complexidade no gerenciamento de transações distribuídas.
- **Desempenho e Escalabilidade:** A mensageria com RabbitMQ garante escalabilidade e tolerância a falhas.
- **Orquestração de Sagas:** A orquestração de Sagas será desafiadora, especialmente no tratamento de falhas.
- **Testes no Firefox:** Garantir compatibilidade no Firefox, testando regularmente durante o desenvolvimento.




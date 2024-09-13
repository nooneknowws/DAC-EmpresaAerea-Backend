# DAC-EmpresaAerea-Backend

## Clonar o respositório.
`git clone + url`

//colocar o numero e link das versões
+ Versão ? do [Node](). 
+ Versão ? do [npm]().
+ Versão ? do [Java](). 
+ Versão ? do [Springboot]().
+ Versão ? do [Angular]().
+ Versão ? do [TS](). 

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

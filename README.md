# Customer Management - Backend

Este repositório contém apenas o **backend** do desafio técnico da TOTVS para cadastro de clientes.

## Sobre o desafio

Desenvolvimento de uma API REST para gerenciamento de clientes, incluindo:

- Cadastro de clientes com os campos:
  - Nome (mínimo 10 caracteres)
  - CPF (válido e único)
  - Telefones (um ou mais, formato válido, não duplicado)
  - Endereços (um ou mais)

## Requisitos Funcionais

- O cliente pode ter múltiplos telefones e endereços.
- Nome do cliente: mínimo de 10 caracteres.
- CPF: deve ser válido (regra oficial), único no sistema e não duplicado.
- Telefones: formato válido, cada telefone deve ser único e não pode ser associado a mais de um cliente.
- Não permitir dados duplicados (nome, CPF ou telefone).

## Tecnologias

- Java 17+
- Spring Boot
- PostgreSQL
- JUnit (testes unitários)
- Swagger (documentação da API)
- Docker (banco de dados)

## Como rodar

1. **Suba o banco de dados PostgreSQL via Docker Compose:**
    ```bash
    docker-compose up
    ```

2. O `application.properties` já está configurado para conectar ao banco de dados do Docker Compose.

3. **Rode a aplicação Java:**  
   Via IDE ou:
    ```bash
    ./mvnw spring-boot:run
    ```
   ou
    ```bash
    mvn spring-boot:run
    ```

4. **Swagger:**  
   Acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Testes

```bash
./mvnw test
# ou
mvn test
```

## Projeto Fullstack

Se você deseja rodar a solução **completa** (backend, frontend e banco de dados) utilizando apenas o Docker, utilize o repositório abaixo:

- [https://github.com/matheus3pires/technical-challenge](https://github.com/matheus3pires/technical-challenge)

Neste repositório fullstack:

- Estão disponíveis **todos os Dockerfiles necessários** dentro da pasta de cada projeto `backend` e `frontend`.  
- O Docker Compose irá, primeiro, buildar o backend e o frontend usando os respectivos Dockerfiles presentes em cada pasta.
- Após o build, o Docker Compose irá subir o banco de dados, o backend e o frontend prontos para uso.

Basta executar:
```bash
docker-compose up
```

para rodar todo o sistema integrado.

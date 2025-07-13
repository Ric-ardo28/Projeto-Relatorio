# Projeto Relatórios - Spring Boot

Este projeto é uma API REST para gerenciamento de relatórios de viagens, desenvolvida com Java, Spring Boot e Maven.

## Sobre

Este sistema foi criado como parte de um projeto pessoal relacionado ao meu trabalho, com o objetivo de facilitar o controle e o registro de relatórios do dia a dia. Pretendo evoluir este projeto continuamente, adicionando novos recursos e melhorias conforme vou aprendendo mais sobre desenvolvimento de software.

Também tenho a intenção de criar um front-end básico para complementar a aplicação e aprimorar meus conhecimentos em outras tecnologias.

## Funcionalidades

- Listar todos os relatórios (com paginação)
- Buscar relatórios por nome do motorista
- Inserir novo relatório
- Atualizar relatório existente
- Remover relatório
- Buscar relatórios por nome (customizado)

## Endpoints

- `GET /relatorio`  
  Lista todos os relatórios paginados.

- `GET /relatorio/{motorista}`  
  Busca relatórios pelo nome do motorista.

- `POST /relatorio`  
  Cria um novo relatório.

- `PUT /relatorio/{id}`  
  Atualiza um relatório existente.

- `DELETE /relatorio/{id}`  
  Remove um relatório.

- `GET /relatorio/busca?nome=valor`  
  Busca relatórios pelo nome (parcial, ignorando maiúsculas/minúsculas).

## Estrutura

- `controller` — Camada de controle (REST)
- `service` — Lógica de negócio
- `repository` — Acesso a dados (JPA)
- `dto` — Objetos de transferência de dados

## Como rodar

1. Clone o repositório
2. Execute `mvn spring-boot:run`
3. Acesse os endpoints via Postman ou navegador

O banco de dados é populado automaticamente com 10 registros de exemplo ao iniciar a aplicação (ver `src/main/resources/import.sql`).

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- Maven
- H2 Database (padrão, pode ser alterado)

---

Desenvolvido por Ricardo.
# AdventurersGuild

Sistema de Gestão de Aventureiros — TP2 Spring Boot

## Pré-requisitos

- Java 21
- Docker
- Maven Wrapper incluso (`./mvnw`)

## 1. Subir o banco de dados

**Mac:**
```bash
docker run -d --name postgres-tp2 -e POSTGRES_PASSWORD=postgres -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-mac

ENDPOINT:

Organizações
GET  /organizacoes
GET  /organizacoes/{id}

Usuários
GET  /usuarios
GET  /usuarios/{id}
GET  /usuarios/{id}/completo
GET  /usuarios/organizacao/{orgId}

Aventureiros
POST   /aventureiros
GET    /aventureiros
GET    /aventureiros/buscar?nome=
GET    /aventureiros/{id}
PUT    /aventureiros/{id}
DELETE /aventureiros/{id}/vinculo
PATCH  /aventureiros/{id}/recrutar
PUT    /aventureiros/{id}/companheiro
DELETE /aventureiros/{id}/companheiro

Missões
POST /missoes
GET  /missoes
GET  /missoes/{id}
POST /missoes/{id}/participantes
GET  /missoes/ranking
GET  /missoes/relatorio
GET  /missoes/top15dias

Produtos (Elasticsearch)
GET /produtos/busca/nome?termo=
GET /produtos/busca/descricao?termo=
GET /produtos/busca/frase?termo=
GET /produtos/busca/fuzzy?termo=
GET /produtos/busca/multicampos?termo=
GET /produtos/busca/com-filtro?termo=&categoria=
GET /produtos/busca/faixa-preco?min=&max=
GET /produtos/busca/avancada?categoria=&raridade=&min=&max=
GET /produtos/agregacoes/por-categoria
GET /produtos/agregacoes/por-raridade
GET /produtos/agregacoes/preco-medio
GET /produtos/agregacoes/faixas-preco

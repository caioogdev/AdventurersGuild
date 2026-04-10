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

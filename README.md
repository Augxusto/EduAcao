# EduAção

Fundação do backend do POC EduDiagnóstico, baseada no documento de planejamento ODS 4.

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker com Compose (para o MongoDB local)

## Execução local

```bash
docker compose up -d
mvn spring-boot:run
```

A aplicação usa `MONGODB_URI` e `SERVER_PORT`; os valores locais padrão estão em `.env.example`.

## Estrutura inicial

- Documentos MongoDB: escola, turma, usuário, problema e histórico do problema.
- Enums: perfil, categoria, prioridade e status.
- Repositórios Spring Data com consultas e índices iniciais.
- Auditoria automática de datas de criação e atualização.

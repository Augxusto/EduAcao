# EduAção

Prova de Conceito (PoC) para identificação e acompanhamento de problemas educacionais, desenvolvida para a AEP de Engenharia de Software, 6º semestre, com foco no **ODS 4 — Educação de Qualidade**.

O projeto implementa uma API REST para cadastrar escolas, usuários e ocorrências, calcular prioridades e acompanhar o status dos problemas. A proposta utiliza o nome **EduAção**.

## Identificação acadêmica

| Campo | Informação                                                                                                                      |
| --- |---------------------------------------------------------------------------------------------------------------------------------|
| Curso | Engenharia de Software                                                                                                          |
| Série e período | 6º semestre — 2026                                                                                                              |
| Título da PoC | EduAção                                                                                                                         |
| Repositório | https://github.com/Augxusto/EduAcao                                                                                             |
| Integrantes e RAs | Guilherme Tamioso Bueno do Prado / RA: 24368069-2 <br/> Rodrigo Augusto da Silva Virgilino / RA: 24139528-2 <br/>Gustavo Cardoso / RA: 24011175-2 |
| Vídeo da primeira entrega | A preencher com o link do YouTube                                                                                               |

## Problema e relação com o ODS 4

Alunos, professores e gestores identificam situações que prejudicam a aprendizagem, mas os relatos podem ficar dispersos e sem acompanhamento. Falta de internet, barreiras de acessibilidade, infraestrutura inadequada e insuficiência de recursos dificultam a definição de prioridades pela gestão escolar.

O EduAção propõe centralizar esses relatos e organizar informações sobre categoria, local, turma, pessoas afetadas e andamento. Sua contribuição ao ODS 4 está no apoio à identificação e ao tratamento de obstáculos à educação de qualidade. O resultado esperado é oferecer informações para decisões da escola.

### Público e perfis previstos

| Perfil | Papel na proposta |
| --- | --- |
| Aluno | Registrar situações e consultar seus relatos. |
| Professor | Relatar e acompanhar problemas de aprendizagem, infraestrutura e recursos. |
| Gestor | Analisar ocorrências, acompanhar prioridades, alterar status e consultar indicadores. |

**Autenticação e restrição de acesso por perfil ainda não estão implementadas.**

## Objetivos e escopo

Demonstrar como o registro estruturado de problemas educacionais pode apoiar sua priorização e resolução. A proposta completa contempla cadastro, classificação, acompanhamento, histórico e indicadores para a gestão.

Não há frontend no atual momento. A demonstração atual pode ser realizada pelo Swagger UI. Interface responsiva, login, telas de detalhes e histórico e painel de indicadores fazem parte da evolução planejada.

## Tecnologias

| Tecnologia | Uso |
| --- | --- |
| Java 21 | Versão-alvo definida no projeto; programação orientada a objetos. |
| Spring Boot 3.4.3 e Spring Web | Inicialização e endpoints REST. |
| Spring Data MongoDB | Persistência em banco NoSQL. |
| Jakarta Bean Validation | Validação dos dados recebidos. |
| Lombok | Geração de métodos e construtores. |
| Springdoc OpenAPI 2.8.14 | Documentação interativa da API. |
| Spring Boot Actuator | Endpoints de saúde e informações. |
| Maven | Dependências, compilação e testes. |
| JUnit 5, Mockito e MockMvc | Testes de serviços e controllers, com dependências simuladas e verificação de respostas HTTP. |
| JaCoCo 0.8.13 | Relatório e verificação de cobertura. |
| Git e GitHub | Versionamento e disponibilização para avaliação. |

A implementação utiliza **MongoDB**.

## Arquitetura e orientação a objetos

```text
Cliente HTTP / Swagger UI
          |
     Controllers      Recebem requisições e validam entradas
          |
       Services       Aplicam regras de negócio
          |
     Repositories     Acessam documentos pelo Spring Data
          |
       MongoDB
```

As entidades encapsulam os dados do domínio. `Escola`, `Usuario` e `Problema` herdam de `EntidadeAuditavel`, que concentra as datas de criação e atualização. Os serviços recebem repositórios por injeção de dependência, e as interfaces de persistência estendem `MongoRepository`. As enumerações delimitam categorias, perfis, prioridades e status.

```text
src/
├── main/
│   ├── java/br/com/eduacao/
│   │   ├── EduAcaoApplication.java
│   │   └── domain/
│   │       ├── controllers/   Endpoints e tratamento de exceções
│   │       ├── enums/         Valores do domínio
│   │       ├── model/         Documentos e auditoria
│   │       ├── repository/    Interfaces de persistência
│   │       └── service/       Regras de negócio
│   └── resources/application.yml
└── test/java/br/com/eduacao/
    ├── domain/controllers/
    │   ├── EscolaControllerTest.java
    │   ├── OpcaoControllerTest.java
    │   └── ProblemaControllerTest.java
    └── service/
        └── ProblemaServiceTest.java
```

## Banco de dados NoSQL

O banco padrão é `eduacao`. Os relacionamentos são referências por identificadores armazenados nos documentos.

| Coleção | Campos principais | Relacionamentos e uso |
| --- | --- | --- |
| `escolas` | `id`, `nome`, `criadoEm`, `atualizadoEm` | Instituição vinculada a usuários e problemas. |
| `usuarios` | `id`, `nome`, `email`, `perfil`, `escolaId`, `ativo`, datas de auditoria | Referencia uma escola por `escolaId`. |
| `problemas` | `id`, `titulo`, `descricao`, `categoria`, `prioridade`, `status`, `local`, `turma`, `usuarioId`, `escolaId`, `quantidadePessoasAfetadas`, datas de auditoria | Referencia escola e autor do relato. |

O atributo Java `id` corresponde ao campo `_id` do MongoDB.

## Regras de negócio

### Categorias

`APRENDIZAGEM`, `TECNOLOGIA`, `INFRAESTRUTURA`, `ACESSIBILIDADE` e `RECURSOS_HUMANOS`.

### Prioridade automática

| Pessoas afetadas | Prioridade inicial |
| --- | --- |
| 1 a 9 | `BAIXA` |
| 10 a 29 | `MEDIA` |
| 30 a 99 | `ALTA` |
| 100 ou mais | `CRITICA` |

Para `ACESSIBILIDADE` e `INFRAESTRUTURA`, a prioridade sobe um nível, limitada a `CRITICA`. Por exemplo, um problema de tecnologia que afeta 30 pessoas recebe prioridade `ALTA`; um de acessibilidade com o mesmo alcance recebe `CRITICA`.

### Status e validações

Todo problema é criado como `ABERTO`. O fluxo de demonstração proposto é:

```text
ABERTO → EM_ANALISE → EM_RESOLUCAO → RESOLVIDO
```

A API aceita qualquer valor válido de status na atualização, sem impor essa ordem e sem gerar histórico automaticamente.

Título, descrição, categoria, local, usuário, escola e quantidade de pessoas afetadas são obrigatórios. A quantidade mínima é 1. Título, local e turma aceitam até 120 caracteres; descrição, até 2.000. `id`, datas de auditoria, prioridade e status inicial são definidos pelo sistema.

## Como executar

### Pré-requisitos

- JDK 21 e Maven instalados e disponíveis no terminal.
- MongoDB em execução, localmente ou em um serviço acessível pela aplicação.
- Git para obter o repositório.

O repositório não inclui Maven Wrapper. Os comandos abaixo usam o Maven instalado na máquina.

### 1. Obter o código

```sh
git clone https://github.com/Augxusto/EduAcao.git
cd EduAcao
```

### 2. Configurar o banco e a porta

| Variável | Valor padrão |
| --- | --- |
| `MONGODB_URI` | `mongodb://localhost:27017/eduacao` |
| `SERVER_PORT` | `8080` |

O arquivo `.env.example` serve como referência. A aplicação não carrega um arquivo `.env` automaticamente; configure as variáveis no terminal ou na configuração de execução da IDE. Se os valores padrão forem adequados, não é necessário defini-las.

PowerShell:

```powershell
$env:MONGODB_URI = "mongodb://localhost:27017/eduacao"
$env:SERVER_PORT = "8080"
```

Linux/macOS:

```sh
export MONGODB_URI='mongodb://localhost:27017/eduacao'
export SERVER_PORT=8080
```

### 3. Iniciar a API

Como o `pom.xml` atual não declara o plugin de execução do Spring Boot, use sua coordenada completa:

```sh
mvn org.springframework.boot:spring-boot-maven-plugin:3.4.3:run
```

Também é possível executar a classe `br.com.eduacao.EduAcaoApplication` pela IDE, após importar o projeto Maven.

### 4. Acessar

- [Swagger UI](http://localhost:8080/swagger-ui.html): operações e exemplos de schemas.
- [OpenAPI JSON](http://localhost:8080/v3/api-docs): especificação da API.
- [Saúde da aplicação](http://localhost:8080/actuator/health): disponibilidade da aplicação e dependências monitoradas.

Ajuste a porta nos endereços caso altere `SERVER_PORT`. Em caso de falha de conexão, confira se o MongoDB está ativo e se a URI está correta.

## Endpoints disponíveis

| Método | Caminho | Finalidade |
| --- | --- | --- |
| POST | `/api/escolas` | Cadastrar escola. |
| GET | `/api/escolas` | Listar escolas. |
| GET | `/api/escolas/{id}` | Consultar escola. |
| POST | `/api/usuarios` | Cadastrar usuário. |
| GET | `/api/usuarios` | Listar usuários. |
| GET | `/api/usuarios/{id}` | Consultar usuário. |
| POST | `/api/problemas` | Registrar problema e calcular prioridade. |
| GET | `/api/problemas` | Listar problemas, sem filtros nesta versão. |
| GET | `/api/problemas/{id}` | Consultar problema. |
| PATCH | `/api/problemas/{id}/status?novoStatus=EM_ANALISE` | Atualizar status por parâmetro de consulta. |
| GET | `/api/opcoes/categorias` | Listar categorias. |
| GET | `/api/opcoes/status` | Listar status. |
| GET | `/api/opcoes/perfis` | Listar perfis. |

Os cadastros retornam HTTP `201`; consultas e atualização de status retornam `200` quando bem-sucedidas. As exceções `IllegalArgumentException`, incluindo referências ou recursos inexistentes, retornam `400` com a chave `erro`.

## Cenário de demonstração

No Swagger UI, execute as operações usando **Try it out** e **Execute**. Os dados abaixo são fictícios. Substitua os identificadores pelos valores retornados nos cadastros.

### 1. Cadastrar uma escola

`POST /api/escolas`

```json
{
  "nome": "Escola Demonstração"
}
```

### 2. Cadastrar um professor

`POST /api/usuarios`

```json
{
  "nome": "Professor Exemplo",
  "email": "professor@example.com",
  "perfil": "PROFESSOR",
  "escolaId": "ID_DA_ESCOLA",
  "ativo": true
}
```

### 3. Registrar o problema

`POST /api/problemas`

```json
{
  "titulo": "Laboratório de informática sem internet",
  "descricao": "A falta de conexão impede a atividade planejada para a turma.",
  "categoria": "TECNOLOGIA",
  "local": "Laboratório de informática",
  "turma": "6º ano B",
  "usuarioId": "ID_DO_USUARIO",
  "escolaId": "ID_DA_ESCOLA",
  "quantidadePessoasAfetadas": 30
}
```

O retorno esperado contém um `id`, prioridade `ALTA` e status `ABERTO`.

### 4. Consultar e acompanhar

1. Liste as ocorrências em `GET /api/problemas`.
2. Consulte a ocorrência em `GET /api/problemas/{id}`.
3. Execute `PATCH /api/problemas/{id}/status` com `novoStatus=EM_ANALISE`.
4. Repita com `EM_RESOLUCAO` e depois `RESOLVIDO`.
5. Consulte novamente o problema para confirmar o status persistido.

## Testes automatizados e cobertura

O site possui **17 testes**, distribuídos em quatro classes, com a inclusão de testes dos controllers de escolas, opções e problemas. Essa atualização amplia a verificação da API para atender à meta de **pelo menos 70% de cobertura de linhas** exigida.

| Classe | Testes | Comportamentos verificados |
| --- | --- | --- |
| `ProblemaServiceTest` | 7 | Cadastro, aumento de prioridade por categoria agravante, rejeição de escola inexistente, listagem, busca por ID, problema inexistente e atualização de status. |
| `EscolaControllerTest` | 3 | Cadastro com retorno HTTP `201`, listagem e consulta por ID com retorno `200`, além da delegação ao serviço. |
| `OpcaoControllerTest` | 3 | Consulta de categorias, status e perfis, verificando retorno `200` e tamanho das listas conforme as enumerações. |
| `ProblemaControllerTest` | 4 | Cadastro, listagem, consulta por ID e atualização de status, verificando respostas HTTP, campos JSON e chamadas ao serviço. |

Os testes de serviço utilizam **JUnit 5 e Mockito**, com repositórios simulados. Os testes de controllers utilizam **MockMvc em modo standalone**, com serviços simulados quando aplicável. A suíte de testes não exige MongoDB ativo nem um servidor HTTP em execução.

### Execução

Executar os testes e gerar o relatório:

```sh
mvn clean test
```

Executar o ciclo completo com a verificação do limite de cobertura:

```sh
mvn clean verify
```

Resultados gerados localmente:

- `target/surefire-reports/`: resultados dos testes.
- `target/site/jacoco/index.html`: relatório navegável de cobertura.
- `target/site/jacoco/jacoco.xml`: relatório para processamento automatizado.

O JaCoCo está configurado para exigir **70% de cobertura de linhas no conjunto do projeto (`BUNDLE`)**, na fase `verify`. `mvn test` gera o relatório, mas não executa a verificação desse limite. A configuração do limite não comprova que a cobertura foi atingida: é necessário executar `mvn clean verify` e apresentar o resultado de cada entrega.

### Evidência da cobertura

Após executar `mvn clean verify`, abra `target/site/jacoco/index.html` e consulte o percentual de linhas na linha **Total**. O ciclo falha se a cobertura medida estiver abaixo de 70%.


# Documentação técnica - EduAção

## Finalidade e escopo

PoC de apoio à identificação e ao acompanhamento de problemas educacionais, alinhada ao ODS 4. A solução permite cadastrar escolas, usuários e problemas, calcular prioridades e acompanhar mudanças de status com histórico persistente. A demonstração é local, pela API REST e pelo Swagger UI.

Tecnologias, versões, instalação, configuração, endpoints, exemplos completos de requisições e execução de testes estão no [README](../README.md). O [quadro de tarefas](QUADRO_DE_TAREFAS.md) registra as pendências da entrega.

## Arquitetura e orientação a objetos

| Camada | Responsabilidade | Exemplos |
| --- | --- | --- |
| Controllers | Receber requisições HTTP, validar entradas e devolver respostas. | `ProblemaController`, `EscolaController`, `UsuarioController`, `OpcaoController` |
| Serviços | Aplicar regras de cadastro, validar referências, calcular prioridade e registrar mudanças. | `ProblemaService`, `EscolaService`, `UsuarioService` |
| Repositórios | Persistir e consultar documentos por interfaces do Spring Data MongoDB. | `ProblemaRepository`, `EscolaRepository`, `UsuarioRepository` |
| Modelo | Representar entidades, dados de auditoria e subdocumentos. | `Problema`, `Escola`, `Usuario`, `EntidadeAuditavel`, `AlteracaoStatus` |

`Escola`, `Usuario` e `Problema` herdam as datas de `EntidadeAuditavel`. A auditoria é habilitada na aplicação por `@EnableMongoAuditing`. `AlteracaoStatus` compõe um problema e não possui coleção própria. Os campos são encapsulados nas classes, com métodos gerados por Lombok. Os serviços recebem suas dependências por construtores; o acesso ao banco usa interfaces que estendem `MongoRepository`. Enumerações representam os valores permitidos de categoria, prioridade, perfil e status.

## Modelagem NoSQL

O banco padrão é `eduacao`. Há três coleções, relacionadas por identificadores. Os IDs são expostos como strings na API; o Spring Data faz o mapeamento para `_id` no MongoDB. Os exemplos a seguir são representações lógicas simplificadas; datas são armazenadas como datas BSON e apresentadas em UTC na API.

| Coleção | Campos | Referências e índices declarados |
| --- | --- | --- |
| `escolas` | `_id`, `nome`, `criadoEm`, `atualizadoEm` | Índice único em `nome`. |
| `usuarios` | `_id`, `nome`, `email`, `perfil`, `escolaId`, `ativo`, `criadoEm`, `atualizadoEm` | `escolaId` referencia `escolas`; índice único em `email`. |
| `problemas` | `_id`, `titulo`, `descricao`, `categoria`, `prioridade`, `status`, `local`, `turma`, `usuarioId`, `escolaId`, `quantidadePessoasAfetadas`, `historicoStatus`, `criadoEm`, `atualizadoEm` | Referências a `usuarios` e `escolas`; índice composto em `escolaId`, `status`, `categoria` e `prioridade`. |

Os índices declarados nas entidades são criados automaticamente pela configuração `auto-index-creation: true`. O índice composto não implica existência de filtros na API: a listagem atual retorna todos os problemas.

Os vínculos são referências por ID, sem chaves estrangeiras do banco. O serviço de usuários verifica a existência da escola; o de problemas verifica a existência da escola e do usuário antes do cadastro. Não há verificação adicional de que ambos pertençam à mesma escola.

### Objetos complexos em problemas

```json
{
  "_id": "ID_DO_PROBLEMA",
  "titulo": "Laboratório sem internet",
  "escolaId": "ID_DA_ESCOLA",
  "usuarioId": "ID_DO_USUARIO",
  "status": "EM_ANALISE",
  "historicoStatus": [
    {
      "statusAnterior": "ABERTO",
      "novoStatus": "EM_ANALISE",
      "alteradoEm": "2026-11-08T19:01:42.204Z"
    }
  ]
}
```

`historicoStatus` é uma lista de subdocumentos incorporados à coleção `problemas`, atendendo ao requisito de pelo menos uma coleção com objetos complexos. Cada evento contém dois status e uma data, em vez de ser apenas uma lista de valores simples. O histórico acompanha o documento do problema no mesmo salvamento e é retornado junto com ele nas consultas.

## Fluxo e regras

1. Cadastrar uma escola e guardar seu ID.
2. Cadastrar um usuário vinculado à escola e guardar seu ID.
3. Cadastrar o problema com escola e usuário existentes. A API define status `ABERTO`, histórico vazio e prioridade automática conforme quantidade de pessoas afetadas e categoria, descrita no README.
4. Atualizar o status pelo endpoint `PATCH /api/problemas/{id}/status?novoStatus=EM_ANALISE`.
5. Consultar o problema por ID para verificar o status e a lista de eventos.

Quando o status muda, o serviço acrescenta um evento com estado anterior, novo estado e `Instant.now()`, atualiza o status e salva o problema. Repetir o status atual devolve o problema sem novo salvamento ou evento. Não há imposição de sequência entre os valores válidos. Em problemas antigos, a próxima mudança inicializa o histórico se necessário, sem inventar eventos anteriores. Histórico, prioridade e status inicial são campos somente de leitura no cadastro pela API.

O documento completo é salvo a cada mudança. A validação realizada cobre o fluxo sequencial da PoC; não foi implementado controle de alterações simultâneas por múltiplos clientes.

As entradas obrigatórias e os limites são validados por Jakarta Bean Validation. Exceções `IllegalArgumentException` lançadas pelos serviços são convertidas em HTTP `400` com mensagem na chave `erro`. O cadastro retorna `201`; consultas e atualização de status retornam `200` quando bem-sucedidos.

## Evolução demonstrável

| Aspecto | Primeira entrega (`9bb2039`) | Segunda entrega (tag `EduAção2.0`) |
| --- | --- | --- |
| Modelagem | Três coleções com referências por IDs e campos simples. | Mantém as coleções e acrescenta uma lista de subdocumentos em problemas. |
| Acompanhamento | Atualiza apenas o status atual. | Registra automaticamente estados anteriores, novos estados e datas das mudanças efetivas. |
| Testes | Suíte existente no GitHub. | Acrescenta verificações do histórico, ordem, repetição, status nulo e compatibilidade com problemas antigos. |
| Evidências | Documentação e comandos da versão anterior. | Registra cobertura de 91,30%, validação real com MongoDB e consulta após reiniciar a API; inclui script reproduzível. |
| Organização | README existente. | README revisado, documentação técnica e quadro de tarefas no repositório. |

O commit `9bb2039`, último commit antes do início da segunda entrega, é a referência usada para a primeira entrega.

A versão revisada da PoC foi publicada no GitHub em 08/11/2026 e integrada à branch `main`. O commit `ce3d0b1` está identificado pela tag `EduAção2.0` e também disponível na branch `EduAção2.0`. A tag preserva o código e a documentação da revisão técnica, anteriores ao registro de publicação nos documentos. O [vídeo da segunda entrega](https://www.youtube.com/watch?v=prf2fHHl3eM) está disponível pelo link e tem 4min49s de duração. O grupo preencheu o modelo de entrega e enviou a atividade para avaliação.

## Execução e evidências de qualidade

Com JDK 21, Maven e MongoDB disponíveis, seguir [Como executar](../README.md#como-executar). Na primeira execução, Maven precisa de acesso à internet para obter dependências ainda não instaladas. `JAVA_HOME` e o Java utilizado pelo Maven devem apontar para JDK 21.

- `mvn clean verify`: executa a suíte e verifica o mínimo de 70% de cobertura de linhas; não exige MongoDB ativo.
- `target/surefire-reports/`: resultados dos testes.
- `target/site/jacoco/index.html` e `jacoco.xml`: cobertura reproduzível, gerada localmente e não versionada.
- `scripts/validar-fluxo.ps1`: validação complementar pela API, com MongoDB real, conforme configuração do README.

Resultado medido em 08/11/2026: 34 testes, nenhuma falha ou erro e 84 de 92 linhas cobertas (91,30%). A validação complementar confirmou três eventos ordenados e preservados após reinício da API.

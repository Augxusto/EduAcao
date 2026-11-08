# Quadro de tarefas - segunda entrega

Atualizado em 08/11/2026. Este quadro passa a registrar o acompanhamento a partir desta etapa. As atividades concluídas abaixo correspondem às implementações e verificações já realizadas; não representam um histórico de movimentações anterior à criação do quadro.

## Método de acompanhamento

O trabalho é dividido em tarefas pequenas, organizadas nas colunas A fazer, Em andamento e Concluído. Antes de começar uma tarefa, o grupo deve registrar seu responsável e movê-la para Em andamento. Ao concluir, deve registrar a evidência e mover a tarefa para Concluído. As atualizações deste arquivo devem acompanhar os commits do projeto. O quadro aplica um fluxo simples inspirado em Kanban, sem atribuir à equipe cerimônias ou práticas não registradas.

## A fazer

| ID | Tarefa | Critério de conclusão | Responsável |
| --- | --- | --- | --- |
| T05 | Confirmar a versão da primeira entrega | Informar o commit, tag ou release efetivamente enviado na primeira entrega. `9bb2039` é a base consultada, ainda sem essa confirmação. | A definir pelo grupo |
| T07 | Gravar e publicar o vídeo final | Vídeo de 3 a 5 minutos mostrando evolução, projeto final, funcionamento, tecnologias, testes, cobertura e resultados; link adicionado ao README. | A definir pelo grupo |
| T08 | Preencher e enviar o modelo de entrega | Curso, série, nomes, RAs, título da PoC, link do vídeo final e link do GitHub conferidos e enviados pelo grupo. | A definir pelo grupo |

## Em andamento

Nenhuma tarefa em andamento neste momento.

## Concluído

| ID | Tarefa | Evidência |
| --- | --- | --- |
| T01 | Adicionar histórico de status como objetos complexos no MongoDB | `Problema.historicoStatus`, com subdocumentos `AlteracaoStatus`, registrado automaticamente em mudanças efetivas. |
| T02 | Testar o histórico e verificar a cobertura | 34 testes sem falhas; `mvn clean verify` aprovado com 91,30% de cobertura de linhas em 08/11/2026. Comandos e caminhos do relatório no README. |
| T03 | Validar o fluxo com MongoDB real | Cadastros, referências, prioridade, auditoria e histórico validados no banco separado; consulta preservada após reiniciar a API. Procedimento e script no README. |
| T04 | Documentar o projeto e criar o quadro de tarefas | README revisado e documentação técnica criada com modelagem, arquitetura, evolução, execução e testes. |
| T09 | Revisar e preparar a versão da PoC para publicação | Revisão concluída em 08/11/2026; 34 testes aprovados e 91,30% de cobertura. Testes de consulta e atualização conferem o histórico nas respostas HTTP. Versão identificada localmente pela tag `entrega-2-poc`. |
| T06 | Publicar a versão revisada no GitHub | Publicação realizada em 08/11/2026: commit `ce3d0b1` integrado à `main`, branch `codex/segunda-entrega` e tag `entrega-2-poc` disponíveis no repositório. |

## Próximas atualizações

Os responsáveis pelas tarefas pendentes serão definidos pelo grupo. A confirmação da versão da primeira entrega, a gravação do vídeo e a submissão acadêmica permanecem pendentes e só devem ser marcadas como concluídas quando houver evidência real.

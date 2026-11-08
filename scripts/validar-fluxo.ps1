param([string]$BaseUrl = 'http://localhost:18080')

$ErrorActionPreference = 'Stop'
function Confirmar($Condicao, $Mensagem) {
    if (-not $Condicao) { throw $Mensagem }
}
function Enviar($Rota, $Dados) {
    Invoke-RestMethod -Uri "$BaseUrl$Rota" -Method Post -ContentType 'application/json; charset=utf-8' -Body ([System.Text.Encoding]::UTF8.GetBytes(($Dados | ConvertTo-Json -Depth 8)))
}

# Use uma instância da API conectada a um banco exclusivo de validação.
# Cada execução cria uma escola, um usuário e um problema fictícios.
$sufixo = [guid]::NewGuid().ToString('N')
$escola = Enviar '/api/escolas' @{nome = "Escola Validacao $sufixo"}
$usuario = Enviar '/api/usuarios' @{
    nome = 'Professor Validacao'; email = "validacao-$sufixo@example.com"
    perfil = 'PROFESSOR'; escolaId = $escola.id; ativo = $true
}
$problema = Enviar '/api/problemas' @{
    titulo = 'Laboratorio sem internet'; descricao = 'Cenario ficticio de validacao'
    categoria = 'TECNOLOGIA'; local = 'Laboratorio'; turma = '6B'
    escolaId = $escola.id; usuarioId = $usuario.id; quantidadePessoasAfetadas = 30
    status = 'RESOLVIDO'; prioridade = 'CRITICA'
    historicoStatus = @(@{statusAnterior = 'ABERTO'; novoStatus = 'RESOLVIDO'})
}
Confirmar ($problema.status -eq 'ABERTO') 'Status inicial incorreto'
Confirmar ($problema.prioridade -eq 'ALTA') 'Prioridade automatica incorreta'
Confirmar ($problema.historicoStatus.Count -eq 0) 'A API aceitou historico enviado pelo cliente'

$anteriores = @('ABERTO', 'EM_ANALISE', 'EM_RESOLUCAO')
$novos = @('EM_ANALISE', 'EM_RESOLUCAO', 'RESOLVIDO')
foreach ($status in $novos) {
    $null = Invoke-RestMethod -Uri "$BaseUrl/api/problemas/$($problema.id)/status?novoStatus=$status" -Method Patch
}
$null = Invoke-RestMethod -Uri "$BaseUrl/api/problemas/$($problema.id)/status?novoStatus=RESOLVIDO" -Method Patch
$consulta = Invoke-RestMethod -Uri "$BaseUrl/api/problemas/$($problema.id)"
Confirmar ($consulta.status -eq 'RESOLVIDO') 'Status final nao persistiu'
Confirmar ($consulta.historicoStatus.Count -eq 3) 'Quantidade de eventos incorreta'
Confirmar ($consulta.escolaId -eq $escola.id -and $consulta.usuarioId -eq $usuario.id) 'Relacionamentos incorretos'
Confirmar ($null -ne $consulta.criadoEm -and $null -ne $consulta.atualizadoEm) 'Datas de auditoria ausentes'
for ($i = 0; $i -lt 3; $i++) {
    $evento = $consulta.historicoStatus[$i]
    Confirmar ($evento.statusAnterior -eq $anteriores[$i] -and $evento.novoStatus -eq $novos[$i]) 'Ordem ou status do historico incorretos'
    Confirmar ($null -ne $evento.alteradoEm) 'Data de alteracao ausente'
}
$consulta | ConvertTo-Json -Depth 8
Write-Output 'VALIDACAO APROVADA: cadastro, referencias, prioridade, auditoria e historico consultado no MongoDB.'

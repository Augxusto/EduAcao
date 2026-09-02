package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "problemas")
@CompoundIndex(
        name = "idx_problema_filtros",
        def = "{'escolaId': 1, 'status': 1, 'categoria': 1, 'prioridade': 1}"
)
public class Problema extends EntidadeAuditavel {

    @Id
    private String id;

    private String titulo;
    private String descricao;
    private CategoriaProblema categoria;
    private PrioridadeProblema prioridade;
    private StatusProblema status = StatusProblema.ABERTO;
    private String local;
    private String turmaId;
    private String usuarioId;
    private String escolaId;
    private Integer quantidadePessoasAfetadas;
    private boolean arquivado;

    public Problema() {
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String valor) {
        titulo = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String valor) {
        descricao = valor;
    }

    public CategoriaProblema getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProblema valor) {
        categoria = valor;
    }

    public PrioridadeProblema getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeProblema valor) {
        prioridade = valor;
    }

    public StatusProblema getStatus() {
        return status;
    }

    public void setStatus(StatusProblema valor) {
        status = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String valor) {
        local = valor;
    }

    public String getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(String valor) {
        turmaId = valor;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String valor) {
        usuarioId = valor;
    }

    public String getEscolaId() {
        return escolaId;
    }

    public void setEscolaId(String valor) {
        escolaId = valor;
    }

    public Integer getQuantidadePessoasAfetadas() {
        return quantidadePessoasAfetadas;
    }

    public void setQuantidadePessoasAfetadas(Integer valor) {
        quantidadePessoasAfetadas = valor;
    }

    public boolean isArquivado() {
        return arquivado;
    }

    public void setArquivado(boolean valor) {
        arquivado = valor;
    }
}

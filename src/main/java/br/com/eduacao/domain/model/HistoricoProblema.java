package br.com.eduacao.domain.model;

import br.com.eduacao.domain.enums.StatusProblema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "historicos_problema")
@CompoundIndex(
        name = "idx_historico_problema_data",
        def = "{'problemaId': 1, 'data': -1}"
)
public class HistoricoProblema {

    @Id
    private String id;

    private String problemaId;
    private StatusProblema statusAnterior;
    private StatusProblema novoStatus;
    private String observacao;
    private String usuarioId;

    @CreatedDate
    private Instant data;

    public HistoricoProblema() {
    }

    public String getId() {
        return id;
    }

    public String getProblemaId() {
        return problemaId;
    }

    public void setProblemaId(String valor) {
        problemaId = valor;
    }

    public StatusProblema getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusProblema valor) {
        statusAnterior = valor;
    }

    public StatusProblema getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(StatusProblema valor) {
        novoStatus = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String valor) {
        observacao = valor;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String valor) {
        usuarioId = valor;
    }

    public Instant getData() {
        return data;
    }
}

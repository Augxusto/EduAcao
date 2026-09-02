package br.com.eduacao.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "turmas")
@CompoundIndex(
        name = "uk_turma_nome_escola",
        def = "{'nome': 1, 'escolaId': 1}",
        unique = true
)
public class Turma extends EntidadeAuditavel {

    @Id
    private String id;

    private String nome;
    private String escolaId;

    public Turma() {
    }

    public Turma(String nome, String escolaId) {
        this.nome = nome;
        this.escolaId = escolaId;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String valor) {
        nome = valor;
    }

    public String getEscolaId() {
        return escolaId;
    }

    public void setEscolaId(String valor) {
        escolaId = valor;
    }
}

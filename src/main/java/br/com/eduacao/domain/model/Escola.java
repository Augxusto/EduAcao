package br.com.eduacao.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "escolas")
public class Escola extends EntidadeAuditavel {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nome;

    public Escola() {
    }

    public Escola(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

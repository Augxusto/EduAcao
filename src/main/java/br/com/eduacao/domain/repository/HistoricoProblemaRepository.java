package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.HistoricoProblema;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoricoProblemaRepository extends MongoRepository<HistoricoProblema, String> {

    List<HistoricoProblema> findByProblemaIdOrderByDataDesc(String problemaId);
}

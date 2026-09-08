package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.Turma;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TurmaRepository extends MongoRepository<Turma, String> {

    List<Turma> findByEscolaId(String escolaId);
}

package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.Escola;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EscolaRepository extends MongoRepository<Escola, String> {
}

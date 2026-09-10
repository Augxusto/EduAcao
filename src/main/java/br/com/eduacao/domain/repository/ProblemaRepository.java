package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.Problema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProblemaRepository extends MongoRepository<Problema, String> {
}

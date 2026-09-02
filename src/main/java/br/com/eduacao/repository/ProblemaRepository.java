package br.com.eduacao.repository;

import br.com.eduacao.domain.enums.CategoriaProblema;
import br.com.eduacao.domain.enums.PrioridadeProblema;
import br.com.eduacao.domain.enums.StatusProblema;
import br.com.eduacao.domain.model.Problema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProblemaRepository extends MongoRepository<Problema, String> {

    Page<Problema> findByEscolaIdAndStatusAndCategoriaAndPrioridade(
            String escolaId,
            StatusProblema status,
            CategoriaProblema categoria,
            PrioridadeProblema prioridade,
            Pageable pageable
    );
}

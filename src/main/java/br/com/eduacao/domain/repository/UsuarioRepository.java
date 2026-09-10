package br.com.eduacao.domain.repository;

import br.com.eduacao.domain.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}

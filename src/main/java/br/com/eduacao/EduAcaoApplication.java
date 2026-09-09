package br.com.eduacao;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition(
        info = @Info(
                title = "EduAção API",
                version = "1.0",
                description = "API para identificação e acompanhamento de problemas educacionais"
        )
)
public class EduAcaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduAcaoApplication.class, args);
    }
}

package br.com.eduacao.domain.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiExceptionHandlerTest {

    @Test
    void deveTratarIllegalArgumentException() {

        ApiExceptionHandler handler = new ApiExceptionHandler();

        IllegalArgumentException exception =
                new IllegalArgumentException("Problema não encontrado");

        ResponseEntity<Map<String, String>> response =
                handler.tratarRecursoNaoEncontrado(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Problema não encontrado", response.getBody().get("erro"));
    }
}
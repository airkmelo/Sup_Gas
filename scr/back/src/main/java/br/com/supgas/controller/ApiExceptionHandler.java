package br.com.supgas.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

// Transforma excecoes em respostas JSON: { "erro": "..." }
// O front le esse "erro" e mostra para o usuario.
@RestControllerAdvice
public class ApiExceptionHandler {

    // @Valid falhou (nome em branco, lista de contatos vazia...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException e) {
        List<String> detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(Map.of("erro", "Dados inválidos", "detalhes", detalhes));
    }

    // Erros que nos mesmos lancamos no service (404, 400)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> status(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
    }

    // Violacao no banco: CPF duplicado, ou registro em uso por outra tabela
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro", "Operação não permitida: CPF já cadastrado ou registro em uso."));
    }
}
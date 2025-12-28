package br.com.controle.estoque.config;

import br.com.controle.estoque.exception.DuplicateUsernameException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
        ));
    }

    // trata username duplicado vindo da Exception
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<?> handleDuplicateUsername(DuplicateUsernameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", e.getMessage()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConstraint(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Não foi possível salvar. Verifique os campos (ex.: preço muito alto)."
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(Map.of(
                "error", "Erro interno",
                "detail", e.getMessage()
        ));
    }
}

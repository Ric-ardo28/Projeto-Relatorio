package br.com.ricardo.jjsul.controller.handlers;

import br.com.ricardo.jjsul.dto.CustomError;
import br.com.ricardo.jjsul.dto.ValidationError;
import br.com.ricardo.jjsul.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

// Esta anotação indica que esta classe vai tratar exceções lançadas pelos controllers do projeto
@ControllerAdvice
public class ControllerExceptionHandler {

    // Este método trata exceções do tipo ResourceNotFoundException (ex: recurso não encontrado)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNoteFound(ResourceNotFoundException e, HttpServletRequest request) {
        // Define o status HTTP como 404 (Not Found)
        HttpStatus status = HttpStatus.NOT_FOUND;
        // Cria um objeto CustomError com informações sobre o erro
        CustomError err = new CustomError(
            Instant.now(), // Momento em que o erro ocorreu
            status.value(), // Código do status HTTP (404)
            e.getMessage(), // Mensagem da exceção
            request.getRequestURI() // URI da requisição que causou o erro
        );
        // Retorna a resposta HTTP com o status e o corpo do erro
        return ResponseEntity.status(status).body(err);
    }

    // Este método trata exceções de validação de argumentos (ex: @Valid falhou)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        // Define o status HTTP como 422 (Unprocessable Entity)
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        // Cria um objeto ValidationError (herda de CustomError) para armazenar os erros de validação
        ValidationError err = new ValidationError(
            Instant.now(), // Momento do erro
            status.value(), // Código do status HTTP (422)
            "Dados invalidos", // Mensagem da exceção
            request.getRequestURI() // URI da requisição
        );
        // Para cada erro de campo, adiciona o nome do campo e a mensagem de erro ao ValidationError
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            err.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        // Retorna a resposta HTTP com o status e o corpo do erro de validação
        return ResponseEntity.status(status).body(err);
    }
}
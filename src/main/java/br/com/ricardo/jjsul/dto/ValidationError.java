// Pacote onde a classe está localizada
package br.com.ricardo.jjsul.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// Classe que representa um erro de validação, estendendo CustomError
public class ValidationError extends CustomError {

    // Lista para armazenar os erros de cada campo inválido
    private List<FieldMessage> errors = new ArrayList<>();

    // Construtor que recebe informações básicas do erro e repassa para a superclasse
    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    // Retorna a lista de erros de campo
    public List<FieldMessage> getErrors() {
        return errors;
    }

    // Adiciona um novo erro de campo à lista, informando o nome do campo e a mensagem de erro
    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
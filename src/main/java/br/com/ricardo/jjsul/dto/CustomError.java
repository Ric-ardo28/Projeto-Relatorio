// Pacote onde a classe está localizada
package br.com.ricardo.jjsul.dto;

import lombok.AllArgsConstructor; // Lombok: gera automaticamente o construtor com todos os argumentos
import lombok.Getter; // Lombok: gera automaticamente os métodos getters para os campos

import java.time.Instant;

// Lombok: cria os métodos get para todos os campos
@Getter
// Lombok: cria o construtor com todos os argumentos (timestamp, status, error, path)
@AllArgsConstructor
public class CustomError {
    // Momento em que o erro ocorreu
    private Instant timestamp;

    // Código do status HTTP (ex: 404, 422)
    private Integer status;

    // Mensagem descritiva do erro
    private String error;

    // Caminho (URI) da requisição que causou o erro
    private String path;
}
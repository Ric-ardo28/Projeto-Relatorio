// Pacote onde a classe está localizada
package br.com.ricardo.jjsul.dto;

import lombok.AllArgsConstructor; // Gera automaticamente o construtor com todos os argumentos
import lombok.Getter; // Gera automaticamente os métodos getters para os campos

@Getter // Lombok: cria os métodos getFieldName() e getMessage()
@AllArgsConstructor // Lombok: cria o construtor com os dois parâmetros (fieldName, message)
public class FieldMessage {

    // Nome do campo que apresentou erro de validação
    private String fieldName;

    // Mensagem descritiva do erro ocorrido no campo
    private String message;
}
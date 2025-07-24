package br.com.ricardo.jjsul.dto;

import br.com.ricardo.jjsul.entities.Relatorio;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDTO {


	private Long id;
	@PastOrPresent(message = "A data não pode ser futura")
	private LocalDate data;
	private LocalTime hora;
	private String motorista;
	private String placaVeiculo;
	private Double valor;

	public RelatorioDTO(Relatorio entity){
		id = entity.getId();
		data = entity.getData();
		hora = entity.getHora();
		motorista = entity.getMotorista();
		placaVeiculo = entity.getPlacaVeiculo();
		valor = entity.getValor();
	}
}

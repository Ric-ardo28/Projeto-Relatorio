package br.com.ricardo.jjsul.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record RelatorioDTO(
		LocalDate data,
		LocalTime hora,
		String motorista,
		String placaVeiculo,
		Double valor
) {}

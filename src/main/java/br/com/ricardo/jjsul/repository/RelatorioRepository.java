package br.com.ricardo.jjsul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ricardo.jjsul.entities.Relatorio;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

	List<Relatorio> findByMotoristaContainingIgnoreCase(String motorista);
}

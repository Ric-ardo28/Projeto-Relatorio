package br.com.ricardo.jjsul.repository;

import br.com.ricardo.jjsul.entities.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

	List<Relatorio> findByMotoristaContainingIgnoreCase(String Motorista);
}

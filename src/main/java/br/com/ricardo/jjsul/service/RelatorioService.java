package br.com.ricardo.jjsul.service;

import br.com.ricardo.jjsul.dto.RelatorioDTO;
import br.com.ricardo.jjsul.entities.Relatorio;
import br.com.ricardo.jjsul.exception.ResourceNotFoundException;
import br.com.ricardo.jjsul.repository.RelatorioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

	private RelatorioRepository relatorioRepository;

	public RelatorioService(RelatorioRepository relatorioRepository) {
		this.relatorioRepository = relatorioRepository;
	}

	@Transactional(readOnly = true)
	public List<RelatorioDTO> findByMotorista(String motorista) {
		List<Relatorio> relatorios = relatorioRepository.findByMotoristaContainingIgnoreCase(motorista);
		return relatorios.stream()
				.map(relatorio -> new RelatorioDTO(
						relatorio.getData(),
						relatorio.getHora(),
						relatorio.getMotorista(),
						relatorio.getPlacaVeiculo(),
						relatorio.getValor()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<RelatorioDTO> findAll(Pageable pageable) {
		Page<Relatorio> relatorios = relatorioRepository.findAll(pageable);
		return relatorios.map(relatorio -> new RelatorioDTO(
				relatorio.getData(),
				relatorio.getHora(),
				relatorio.getMotorista(),
				relatorio.getPlacaVeiculo(),
				relatorio.getValor()));
	}

	@Transactional
	public RelatorioDTO insert(RelatorioDTO dto) {
		Relatorio relatorio = new Relatorio();
		relatorio.setData(dto.data());
		relatorio.setHora(dto.hora());
		relatorio.setMotorista(dto.motorista());
		relatorio.setPlacaVeiculo(dto.placaVeiculo());
		relatorio.setValor(dto.valor());

		relatorio = relatorioRepository.save(relatorio);

		return new RelatorioDTO(
				relatorio.getData(),
				relatorio.getHora(),
				relatorio.getMotorista(),
				relatorio.getPlacaVeiculo(),
				relatorio.getValor());
	}

	@Transactional
	public RelatorioDTO update(Long id, RelatorioDTO dto) {

		Relatorio relatorio = relatorioRepository.getReferenceById(id);
		relatorio.setData(dto.data());
		relatorio.setHora(dto.hora());
		relatorio.setMotorista(dto.motorista());
		relatorio.setPlacaVeiculo(dto.placaVeiculo());
		relatorio.setValor(dto.valor());

		relatorio = relatorioRepository.save(relatorio);
		return new RelatorioDTO(
				relatorio.getData(),
				relatorio.getHora(),
				relatorio.getMotorista(),
				relatorio.getPlacaVeiculo(),
				relatorio.getValor());

	}
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!relatorioRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			relatorioRepository.deleteById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Erro ao deletar o recurso");
		}

	}
}
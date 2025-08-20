package br.com.ricardo.jjsul.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.ricardo.jjsul.dto.RelatorioDTO;
import br.com.ricardo.jjsul.entities.Relatorio;
import br.com.ricardo.jjsul.exception.ResourceNotFoundException;
import br.com.ricardo.jjsul.repository.RelatorioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RelatorioService {

	private final RelatorioRepository relatorioRepository;

	public RelatorioService(RelatorioRepository relatorioRepository) {
		this.relatorioRepository = relatorioRepository;
	}

	@Transactional(readOnly = true)
	public List<RelatorioDTO> findByMotorista(String motorista) {
		List<Relatorio> relatorios = relatorioRepository.findByMotoristaContainingIgnoreCase(motorista);
		if (relatorios.isEmpty()) {
			throw new ResourceNotFoundException("Motorista não encontrado: " + motorista);
		}
		return relatorios.stream()
				.map(RelatorioDTO::new)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<RelatorioDTO> findAll(Pageable pageable) {
		Pageable pageableOrder = pageable.getSort().isSorted() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("data").ascending());
		Page<Relatorio> relatorios = relatorioRepository.findAll(pageableOrder);
		return relatorios.map(RelatorioDTO::new);
	}

	@Transactional
	public RelatorioDTO insert(RelatorioDTO dto) {
		Relatorio entity = new Relatorio();
		entity.setData(dto.getData());
		entity.setHora(dto.getHora());
		entity.setMotorista(dto.getMotorista());
		entity.setPlacaVeiculo(dto.getPlacaVeiculo());
		entity.setValor(dto.getValor());


		entity = relatorioRepository.save(entity);

		return new RelatorioDTO(entity);
	}


	@Transactional
	public RelatorioDTO update(Long id, RelatorioDTO dto) {
		try {
			Relatorio entity = relatorioRepository.getReferenceById(id);
			entity.setData(dto.getData());
			entity.setHora(dto.getHora());
			entity.setMotorista(dto.getMotorista());
			entity.setPlacaVeiculo(dto.getPlacaVeiculo());
			entity.setValor(dto.getValor());
			entity = relatorioRepository.save(entity);
			return new RelatorioDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso nao encontrado");
		}


	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!relatorioRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		relatorioRepository.deleteById(id);
	}
}

package br.com.ricardo.jjsul.service;

import br.com.ricardo.jjsul.dto.RelatorioDTO;
import br.com.ricardo.jjsul.entities.Relatorio;
import br.com.ricardo.jjsul.exception.ResourceNotFoundException;
import br.com.ricardo.jjsul.repository.RelatorioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
	    if (relatorios.isEmpty()) {
	        throw new ResourceNotFoundException("Motorista não encontrado: " + motorista);
	    }
	    return relatorios.stream()
	            .map(RelatorioDTO::new)
	            .collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<RelatorioDTO> findAll(Pageable pageable) {
		Page<Relatorio> relatorios = relatorioRepository.findAll(pageable);
		return relatorios.map((java.util.function.Function<? super Relatorio, ? extends RelatorioDTO>) RelatorioDTO::new);
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
			throw new ResourceNotFoundException("Recurso não encontrado");
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

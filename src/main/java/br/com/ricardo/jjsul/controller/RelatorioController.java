package br.com.ricardo.jjsul.controller;

import br.com.ricardo.jjsul.dto.RelatorioDTO;
import br.com.ricardo.jjsul.service.RelatorioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/relatorio")
public class RelatorioController {
	@Autowired
	private RelatorioService relatorioService;

	@GetMapping
	public ResponseEntity<org.springframework.data.domain.Page<RelatorioDTO>> findAll(Pageable pageable) {
		Page<RelatorioDTO> relatorios = relatorioService.findAll(pageable);
		return ResponseEntity.ok(relatorios); // 200 OK com a paginação
	}
	
	@GetMapping(value = "/buscar/{motorista}")
	public ResponseEntity<List<RelatorioDTO>> findByMotorista(@PathVariable String motorista) {
		return ResponseEntity.ok(relatorioService.findByMotorista(motorista)); // 200 OK com a lista
	}
	@PostMapping
	public ResponseEntity<RelatorioDTO> insert(@Valid @RequestBody RelatorioDTO dto) {
		RelatorioDTO relatorio = relatorioService.insert(dto);
		if (relatorio == null) {
			return ResponseEntity.badRequest().build(); // 400 Bad Request
		}
		return ResponseEntity.status(201).body(relatorio); // 201 Created com o novo relatorio
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<RelatorioDTO> update(@PathVariable Long id, @Valid @RequestBody RelatorioDTO dto) {
		RelatorioDTO relatorio = relatorioService.update(id, dto);
		if (relatorio == null) {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
		return ResponseEntity.ok(relatorio); // 200 OK com o relatorio atualizado
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		relatorioService.delete(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}
}

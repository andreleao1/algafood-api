package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;

	@GetMapping
	public ResponseEntity<List<Estado>> listar() {
		return ResponseEntity.ok(this.estadoService.listar());
	}

	@GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscar(@PathVariable long estadoId) {
		Estado estadoPesquisado = this.estadoService.buscar(estadoId);

		if (Objects.nonNull(estadoPesquisado)) {
			return ResponseEntity.ok(estadoPesquisado);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Estado> salvar(@RequestBody  @Valid Estado estado) {
		this.estadoService.salvar(estado);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{estadoId}")
	public ResponseEntity<Estado> atualizar(@PathVariable long estadoId, @RequestBody @Valid Estado estado) {
		Estado estadoPesquisado = this.estadoService.buscar(estadoId);

		if (Objects.nonNull(estadoPesquisado)) {
			BeanUtils.copyProperties(estado, estadoPesquisado, "id");
			this.estadoService.salvar(estadoPesquisado);
			return ResponseEntity.ok(estadoPesquisado);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{estadoId}")
	public ResponseEntity<Void> remover(@PathVariable long estadoId) {
		this.estadoService.excluir(estadoId);
		return ResponseEntity.noContent().build();
	}
}

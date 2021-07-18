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

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<List<Cidade>> listar() {
		return ResponseEntity.ok(this.cidadeService.listar());
	}

	@GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable long cidadeId) {
		Cidade cidadePesquisada = this.cidadeService.buscar(cidadeId);

		if (Objects.nonNull(cidadePesquisada)) {
			return ResponseEntity.ok(cidadePesquisada);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Cidade> salvar(@RequestBody @Valid Cidade cidade) {
		this.cidadeService.salvar(cidade);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{cidadeId}")
	public ResponseEntity<Cidade> atualizar(@PathVariable long cidadeId, @RequestBody  @Valid Cidade cidade) {
		Cidade cidadePesquisada = this.cidadeService.buscar(cidadeId);

		if (Objects.nonNull(cidadePesquisada)) {
			BeanUtils.copyProperties(cidade, cidadePesquisada, "id");
			this.cidadeService.salvar(cidadePesquisada);
			return ResponseEntity.ok(cidadePesquisada);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Void> remover(@PathVariable long cidadeId) {
		this.cidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}

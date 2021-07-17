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

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaService cozinhaService;

	@GetMapping
	public ResponseEntity<List<Cozinha>> listar() {
		return ResponseEntity.ok(this.cozinhaService.listar());
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable long cozinhaId) {
		Cozinha cozinhaPesquisada = this.cozinhaService.buscar(cozinhaId);

		if (Objects.nonNull(cozinhaPesquisada)) {
			return ResponseEntity.ok(cozinhaPesquisada);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/buscar-por-nome")
	public ResponseEntity<List<Cozinha>> buscarPorNome(String nome) {
		List<Cozinha> cozinhasPesquisadas = this.cozinhaService.buscarPorNome(nome);

		if (Objects.nonNull(cozinhasPesquisadas)) {
			return ResponseEntity.ok(cozinhasPesquisadas);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Cozinha> salvar(@RequestBody @Valid Cozinha cozinha) {
		this.cozinhaService.salvar(cozinha);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable long cozinhaId, @RequestBody @Valid Cozinha cozinha) {
		Cozinha cozinhaPesquisada = this.cozinhaService.buscar(cozinhaId);

		if (Objects.nonNull(cozinhaPesquisada)) {
			BeanUtils.copyProperties(cozinha, cozinhaPesquisada, "id");
			this.cozinhaService.salvar(cozinhaPesquisada);
			return ResponseEntity.ok(cozinhaPesquisada);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Void> remover(@PathVariable long cozinhaId) {
		this.cozinhaService.excluir(cozinhaId);
		return ResponseEntity.noContent().build();
	}
}

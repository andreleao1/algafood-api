package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.GrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private GrupoService grupoService;

	@GetMapping
	public ResponseEntity<List<Grupo>> listar() {
		return ResponseEntity.ok(this.grupoService.listar());
	}

	@GetMapping("/{grupoId}")
	public ResponseEntity<Grupo> buscar(@PathVariable long grupoId) {
		Grupo grupoPesquisado = this.grupoService.buscar(grupoId);

		if (Objects.nonNull(grupoPesquisado)) {
			return ResponseEntity.ok(grupoPesquisado);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Grupo> salvar(@RequestBody @Valid Grupo grupo) {
		return ResponseEntity.ok(this.grupoService.salvar(grupo));
	}

	@PutMapping("/{grupoId}")
	public ResponseEntity<Grupo> atualizar(@PathVariable @Valid long grupoId, @RequestBody Grupo grupo) {
		Grupo grupoPesquisado = this.grupoService.buscar(grupoId);

		if (Objects.nonNull(grupoPesquisado)) {
			BeanUtils.copyProperties(grupo, grupoPesquisado, "id");
			this.grupoService.salvar(grupoPesquisado);
			return ResponseEntity.ok(grupoPesquisado);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{grupoId}")
	public ResponseEntity<Void> remover(@PathVariable long grupoId) {
		this.grupoService.excluir(grupoId);
		return ResponseEntity.noContent().build();
	}
}

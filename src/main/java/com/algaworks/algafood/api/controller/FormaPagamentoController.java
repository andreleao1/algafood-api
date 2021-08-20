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

import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
	@Autowired
	private FormaPagamentoService formaPagamentoService;

	@GetMapping
	public ResponseEntity<List<FormaPagamento>> listar() {
		return ResponseEntity.ok(this.formaPagamentoService.listar());
	}

	@GetMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamento> buscar(@PathVariable long formaPagamentoId) {
		FormaPagamento formaPagamentoPesquisada = this.formaPagamentoService.buscar(formaPagamentoId);

		if (Objects.nonNull(formaPagamentoPesquisada)) {
			return ResponseEntity.ok(formaPagamentoPesquisada);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<FormaPagamento> salvar(@RequestBody @Valid FormaPagamento formaPagamento) {
		return ResponseEntity.ok(this.formaPagamentoService.salvar(formaPagamento));
	}

	@PutMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamento> atualizar(@PathVariable long formaPagamentoId,
			@RequestBody @Valid FormaPagamento formaPagamento) {
		FormaPagamento formaPagamentoPesquisada = this.formaPagamentoService.buscar(formaPagamentoId);

		if (Objects.nonNull(formaPagamentoPesquisada)) {
			BeanUtils.copyProperties(formaPagamento, formaPagamentoPesquisada, "id");
			this.formaPagamentoService.salvar(formaPagamentoPesquisada);
			return ResponseEntity.ok(formaPagamentoPesquisada);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{formaPagamentoId}")
	public ResponseEntity<Void> remover(@PathVariable long formaPagamentoId) {
		this.formaPagamentoService.excluir(formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
}

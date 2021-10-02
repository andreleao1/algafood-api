package com.algaworks.algafood.api.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<Page<Produto>> listarProdutos(Pageable pageable) {
		return ResponseEntity.ok(this.produtoService.listar(pageable));
	}
	
	@GetMapping("/{produtoId}")
	public ResponseEntity<Produto> buscarProduto(@PathVariable long produtoId) {
		return ResponseEntity.ok(this.produtoService.buscar(produtoId));
	}
	
	@PostMapping
	public ResponseEntity<Produto> salvar(@RequestBody @Valid Produto produto) {
		return ResponseEntity.ok(this.produtoService.salvar(produto));
	}
	
	@PutMapping("/{produtoId}")
	public ResponseEntity<Produto> atualizar(@PathVariable long produtoId, @RequestBody @Valid Produto produto) {
		Produto produtoPesquisado = this.produtoService.buscar(produtoId);

		if (Objects.nonNull(produtoPesquisado)) {
			BeanUtils.copyProperties(produto, produtoPesquisado, "id");
			this.produtoService.salvar(produtoPesquisado);
			return ResponseEntity.ok(produtoPesquisado);
		}

		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{produtoId}")
	public ResponseEntity<?> remover(@PathVariable long produtoId) {
		this.produtoService.excluir(produtoId);
		return ResponseEntity.noContent().build();
	}
}

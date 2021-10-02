package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar registro(s) de restaurante(s) com %s %d";

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Page<Produto> listar(Pageable pageable) {
		return this.produtoRepository.findAll(pageable);
	}

	@Transactional
	public Produto buscar(long produtoId) {
		Produto produto = this.produtoRepository.findById(produtoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, "id", produtoId)));
		return produto;
	}

	@Transactional
	public Produto salvar(Produto produto) {
		return this.produtoRepository.save(produto);
	}

	@Transactional
	public void excluir(long produtoId) {
		if (this.produtoRepository.existsById(produtoId)) {
			try {
				this.produtoRepository.deleteById(produtoId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException((String
						.format("Produto de código %d não pode ser removida, pois está em uso.", produtoId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, produtoId));
		}
	}
}

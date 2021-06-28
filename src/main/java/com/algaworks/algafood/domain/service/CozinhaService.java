package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar registro(s) de cozinha(s) com %s %d";

	@Autowired
	private CozinhaRepository cozinhaRepository;

	public List<Cozinha> listar() {
		return this.cozinhaRepository.findAll();
	}

	public Cozinha buscar(long cozinhaId) {
		Cozinha cozinha = this.cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, "id", cozinhaId)));
		return cozinha;
	}

	public List<Cozinha> buscarPorNome(String nome) {
		return this.cozinhaRepository.findByNomeContainingIgnoreCase(nome)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, "nome", nome)));
	}

	public Cozinha salvar(Cozinha cozinha) {
		return this.cozinhaRepository.save(cozinha);
	}

	public void excluir(long cozinhaId) {
		if (this.cozinhaRepository.existsById(cozinhaId)) {
			try {
				this.cozinhaRepository.deleteById(cozinhaId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(
						(String.format("Cozinha de código %d não pode ser removida, pois está em uso.", cozinhaId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, cozinhaId));
		}
	}
}

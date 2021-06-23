package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CidadeService {
	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar uma cozinha com id %d";

	@Autowired
	private CidadeRepository cidadeRepository;

	public List<Cidade> listar() {
		return this.cidadeRepository.findAll();
	}

	public Cidade buscar(long cidadeId) {
		Cidade cidade = this.cidadeRepository.findById(cidadeId).orElseThrow(
				() -> new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, cidadeId)));
		return cidade;
	}

	public Cidade salvar(Cidade cidade) {
		return this.cidadeRepository.save(cidade);
	}

	public void excluir(long cidadeId) {
		if (this.cidadeRepository.existsById(cidadeId)) {
			try {
				this.cidadeRepository.deleteById(cidadeId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(
						(String.format("Cozinha de código %d não pode ser removida, pois está em uso.", cidadeId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, cidadeId));
		}
	}
}

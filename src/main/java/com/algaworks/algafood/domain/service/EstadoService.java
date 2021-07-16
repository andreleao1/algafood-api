package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar uma cozinha com id %d";

	@Autowired
	private EstadoRepository estadoRepository;

	public List<Estado> listar() {
		return this.estadoRepository.findAll();
	}

	public Estado buscar(long estadoId) {
		Estado estado = this.estadoRepository.findById(estadoId).orElseThrow(
				() -> new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, estadoId)));
		return estado;
	}

	public Estado salvar(Estado estado) {
		return this.estadoRepository.save(estado);
	}

	public void excluir(long estadoId) {
		if (this.estadoRepository.existsById(estadoId)) {
			try {
				this.estadoRepository.deleteById(estadoId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(
						(String.format("O estado de código %d não pode ser removida, pois está em uso.", estadoId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, estadoId));
		}
	}
}

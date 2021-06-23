package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar uma cozinha com id %d";

	@Autowired
	private RestauranteRepository restauranteRepository;

	public List<Restaurante> listar() {
		return this.restauranteRepository.findAll();
	}

	public Restaurante buscar(long restauranteId) {
		Restaurante restaurante = this.restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, restauranteId)));
		return restaurante;
	}

	public Restaurante salvar(Restaurante restaurante) {
		return this.restauranteRepository.save(restaurante);
	}

	public void excluir(long restauranteId) {
		if (this.restauranteRepository.existsById(restauranteId)) {
			try {
				this.restauranteRepository.deleteById(restauranteId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException((String
						.format("Cozinha de código %d não pode ser removida, pois está em uso.", restauranteId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, restauranteId));
		}
	}
}

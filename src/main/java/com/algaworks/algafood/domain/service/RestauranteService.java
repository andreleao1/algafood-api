package com.algaworks.algafood.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar registro(s) de restaurante(s) com %s %d";

	private static final String MENSAGEM_RESTAURANTE_NAO_ENCONTRADO_PELA_TAXA_FRETE = "Não foi possível encontrar registro(s) de restaurante(s) com a taxa frete entre %d e %d.";

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA_PELO_NOME_OU_PELO_NOME_DA_COZINHA = "Não foi possível encontrar registro(s) de restaurante(s) pelo nome %s nem pela cozinha %s.";
	
	@Autowired
	private RestauranteRepository restauranteRepository;

	public List<Restaurante> listar() {
		return this.restauranteRepository.findAll();
	}

	@Transactional
	public Restaurante buscar(long restauranteId) {
		Restaurante restaurante = this.restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, "id", restauranteId)));
		return restaurante;
	}

	public List<Restaurante> buscarPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {
		List<Restaurante> restaurantesPesquisados = this.restauranteRepository
				.findAllByTaxaFreteBetween(taxaInicial, taxaFinal).orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_RESTAURANTE_NAO_ENCONTRADO_PELA_TAXA_FRETE, taxaInicial, taxaFinal)));

		return restaurantesPesquisados;
	}
	
	public List<Restaurante> buscarPorNomeOuNomeCozinha(String nomeRestaurante, String nomeCozinha) {
		List<Restaurante> restaurantesPesquisados = this.restauranteRepository
				.findAllByNomeOrCozinhaNome(nomeRestaurante, nomeCozinha)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA_PELO_NOME_OU_PELO_NOME_DA_COZINHA,
								nomeRestaurante, nomeCozinha)));

		return restaurantesPesquisados;
	}

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		return this.restauranteRepository.save(restaurante);
	}

	@Transactional
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
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restaurantePesquisado = buscar(restauranteId);
		restaurantePesquisado.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restaurantePesquisado = buscar(restauranteId);
		restaurantePesquisado.inativar();
	}
}

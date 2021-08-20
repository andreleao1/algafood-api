package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteDTO {

	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaDTO cozinha;
	private EnderecoRestauranteDTO endereco;

	public RestauranteDTO(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha, EnderecoRestauranteDTO endereco) {
		this.id = id;
		this.nome = nome;
		this.taxaFrete = taxaFrete;
		this.cozinha = cozinha;
		this.endereco = endereco;
	}

}

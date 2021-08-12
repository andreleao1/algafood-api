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

	public RestauranteDTO(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha) {
		this.id = id;
		this.nome = nome;
		this.taxaFrete = taxaFrete;
		this.cozinha = cozinha;
	}

}

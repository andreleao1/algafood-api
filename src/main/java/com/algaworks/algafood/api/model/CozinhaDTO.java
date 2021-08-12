package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaDTO {

	private Long id;
	private String nome;

	public CozinhaDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}

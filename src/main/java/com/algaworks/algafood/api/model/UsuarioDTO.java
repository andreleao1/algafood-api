package com.algaworks.algafood.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UsuarioDTO {

	private long id;
	private String nome;
	private String email;

	public UsuarioDTO(long id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

}

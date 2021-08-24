package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.UsuarioDTO;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listar() {
		List<Usuario> usuarios = this.usuarioService.listar();

		if (!CollectionUtils.isEmpty(usuarios)) {
			List<UsuarioDTO> usuariosDto = usuarios.stream().map(usuario -> {
				return parseToUsuarioDto(usuario);
			}).collect(Collectors.toList());
			return ResponseEntity.ok(usuariosDto);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{usuarioId}")
	public ResponseEntity<UsuarioDTO> buscar(@PathVariable long usuarioId) {
		Usuario usuarioPesquisado = this.usuarioService.buscar(usuarioId);
		UsuarioDTO usuarioDto = parseToUsuarioDto(usuarioPesquisado);
		return ResponseEntity.ok(usuarioDto);
	}

	@PostMapping
	public ResponseEntity<UsuarioDTO> salvar(@Valid @RequestBody Usuario usuario) {
		Usuario usuarioSalvo = this.usuarioService.salvar(usuario);

		if (Objects.nonNull(usuarioSalvo)) {
			return ResponseEntity.ok(parseToUsuarioDto(usuarioSalvo));
		}
		return ResponseEntity.badRequest().build();
	}
	
//	public ResponseEntity<UsuarioDTO> atualizar(@Valid @RequestBody Usuario usaurio) {
//		
//	}

	private UsuarioDTO parseToUsuarioDto(Usuario usuario) {
		return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
	}
}

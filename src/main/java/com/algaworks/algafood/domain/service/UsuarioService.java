package com.algaworks.algafood.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private static final String MSG_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar usuário com o id %d.";
	
	private static final String MSG_ENTIDADE_EM_USO = "Não foi possível excluir o usuário com o id %d pois o mesmo está em uso.";
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> listar() {
		return this.usuarioRepository.findAll();
	}

	public Usuario buscar(long usuarioId) {
		Usuario usuario = this.usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntidadeNaoEncontradaException(String.format(MSG_ENTIDADE_NAO_ENCONTRADA, usuarioId)));
		return usuario;
	}

	@Transactional
	public Usuario salvar(Usuario usuario) {
		if (usuario.getId() < 0) {
			verificarEmail(usuario.getEmail());			
		}
		return this.usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void excluir(long usuarioId) {
		if (this.usuarioRepository.existsById(usuarioId)) {
			try {
				this.usuarioRepository.deleteById(usuarioId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(String.format(MSG_ENTIDADE_EM_USO, usuarioId));
			}
		}
	}
	
	@Transactional
	public void atualizarSenha(long usaurioId, Map<String,String> corpoRequisicao) {
		Usuario usuarioPesquisado = buscar(usaurioId);
		String senhaAtual = corpoRequisicao.get("senhaAtual");
		String novaSenha = corpoRequisicao.get("novaSenha");
		if (usuarioPesquisado.getSenha().equals(senhaAtual)) {
			usuarioPesquisado.atualizarSenha(novaSenha);
		}
	}
	
	private void verificarEmail(String email) {
		boolean isEmailValido = this.usuarioRepository.existsByEmail(email);
		
		if(isEmailValido) {
			throw new NegocioException("Já existe usuário cadastrado com este email.");
		}
	}
}

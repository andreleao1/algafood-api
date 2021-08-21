package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class GrupoService {

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar registro(s) de grupo(s) com id %d";

	@Autowired
	private GrupoRepository grupoRepository;

	public List<Grupo> listar() {
		return this.grupoRepository.findAll();
	}

	public Grupo buscar(long grupoId) {
		Grupo grupo = this.grupoRepository.findById(grupoId).orElseThrow(() -> new EntidadeNaoEncontradaException(
				String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, "id", grupoId)));
		return grupo;
	}

	@Transactional
	public Grupo salvar(Grupo grupo) {
		return this.grupoRepository.save(grupo);
	}

	@Transactional
	public void excluir(long grupoId) {
		if (this.grupoRepository.existsById(grupoId)) {
			try {
				this.grupoRepository.deleteById(grupoId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(
						(String.format("Grupo de código %d não pode ser removido, pois está em uso.", grupoId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, grupoId));
		}
	}
}

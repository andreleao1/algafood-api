package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;

	private static final String MENSAGEM_ENTIDADE_NAO_ENCONTRADA = "Não foi possível encontrar uma forma de pagamento com id %d";

	public List<FormaPagamento> listar() {
		return this.formaPagamentoRepository.findAll();
	}

	public FormaPagamento buscar(long formaPagamentoId) {
		FormaPagamento formaPagamento = this.formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, formaPagamentoId)));
		return formaPagamento;
	}

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return this.formaPagamentoRepository.save(formaPagamento);
	}

	@Transactional
	public void excluir(long formaPagamentoId) {
		if (this.formaPagamentoRepository.existsById(formaPagamentoId)) {
			try {
				this.formaPagamentoRepository.deleteById(formaPagamentoId);
			} catch (DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException((String
						.format("O estado de código %d não pode ser removida, pois está em uso.", formaPagamentoId)));
			}
		} else {
			throw new EntidadeNaoEncontradaException(String.format(MENSAGEM_ENTIDADE_NAO_ENCONTRADA, formaPagamentoId));
		}
	}
}

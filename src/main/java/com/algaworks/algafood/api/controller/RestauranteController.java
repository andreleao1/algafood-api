package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhaDTO;
import com.algaworks.algafood.api.model.EnderecoRestauranteDTO;
import com.algaworks.algafood.api.model.RestauranteDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteService restauranteService;

	@GetMapping
	public ResponseEntity<List<Restaurante>> listar() {
		return ResponseEntity.ok(this.restauranteService.listar());
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<RestauranteDTO> buscar(@PathVariable long restauranteId) {
		Restaurante restaurantePesquisado = this.restauranteService.buscar(restauranteId);

		if (Objects.nonNull(restaurantePesquisado)) {
			RestauranteDTO restauranteDto = criarRestauranteDTO(restaurantePesquisado);
			return ResponseEntity.ok(restauranteDto);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/buscar-por-taxa-frete")
	public ResponseEntity<List<Restaurante>> buscarPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {
		List<Restaurante> restaurantesPesquisados = this.restauranteService.buscarPorTaxaFrete(taxaInicial, taxaFinal);

		if (Objects.nonNull(restaurantesPesquisados)) {
			return ResponseEntity.ok(restaurantesPesquisados);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/buscar-por-nome-ou-nome-cozinha")
	public ResponseEntity<List<Restaurante>> buscarPorNomeOuNomeCozinha(String nomeRestaurante, String nomeCozinha) {
		List<Restaurante> restaurantesPesquisados = this.restauranteService.buscarPorNomeOuNomeCozinha(nomeRestaurante,
				nomeCozinha);

		if (Objects.nonNull(restaurantesPesquisados)) {
			return ResponseEntity.ok(restaurantesPesquisados);
		}

		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/formas-pagamento/{restauranteId}")
	public ResponseEntity<List<FormaPagamento>> listarFormasPagamento(@PathVariable long restauranteId) {
		Restaurante restaurante = this.restauranteService.buscar(restauranteId);
		
		return ResponseEntity.ok(restaurante.getFormasPagamento());
	}

	@PostMapping
	public ResponseEntity<Restaurante> salvar(@RequestBody @Valid Restaurante restaurante) {
		this.restauranteService.salvar(restaurante);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> atualizar(@PathVariable @Valid long restauranteId,
			@RequestBody Restaurante restaurante) {
		Restaurante restaurantePesquisado = this.restauranteService.buscar(restauranteId);

		if (Objects.nonNull(restaurantePesquisado)) {
			BeanUtils.copyProperties(restaurante, restaurantePesquisado, "id", "formasPagamento", "endereco",
					"dataCadastro", "produtos");
			this.restauranteService.salvar(restaurantePesquisado);
			return ResponseEntity.ok(restaurantePesquisado);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<Void> remover(@PathVariable long restauranteId) {
		this.restauranteService.excluir(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{restauranteId}/ativar")
	public ResponseEntity<Void> ativarRestaurante(@PathVariable Long restauranteId) {
		this.restauranteService.ativar(restauranteId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{restauranteId}/inativar")
	public ResponseEntity<Void> inativarRestaurante(@PathVariable Long restauranteId) {
		this.restauranteService.inativar(restauranteId);
		return ResponseEntity.ok().build();
	}

	private RestauranteDTO criarRestauranteDTO(Restaurante restaurante) {
		CozinhaDTO cozinhaDTO = new CozinhaDTO(restaurante.getCozinha().getId(), restaurante.getCozinha().getNome());
		EnderecoRestauranteDTO enderecoRestauranteDTO = criarRestauranteEnderecoDTO(restaurante);
		RestauranteDTO restauranteDTO = new RestauranteDTO(restaurante.getId(), restaurante.getNome(),
				restaurante.getTaxaFrete(), cozinhaDTO, enderecoRestauranteDTO);
		return restauranteDTO;
	}

	private EnderecoRestauranteDTO criarRestauranteEnderecoDTO(Restaurante restaurante) {
		return new EnderecoRestauranteDTO(restaurante.getEndereco().getCep(), restaurante.getEndereco().getLogradouro(),
				restaurante.getEndereco().getNumero(), restaurante.getEndereco().getComplemento(),
				restaurante.getEndereco().getBairro(), restaurante.getEndereco().getCidade().getNome(),
				restaurante.getEndereco().getCidade().getEstado().getNome());

	}
}

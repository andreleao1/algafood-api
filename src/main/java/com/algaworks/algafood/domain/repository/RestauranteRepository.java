package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

	Optional<List<Restaurante>> findAllByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	Optional<List<Restaurante>> findAllByNomeOrCozinhaNome(String nomeRestaurante, String nomeCozinha);
	
	@Query("from Restaurante r join r.cozinha join fetch r.formasPagamento")
	List<Restaurante> findAll();
}

package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>{

	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long id);
	
	Optional<Restaurante> getFirstByNomeContaining(String nome);
	
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
}

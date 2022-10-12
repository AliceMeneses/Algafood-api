package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.infrastructure.repository.RestauranteRepositoryQueries;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries{

	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

//	@Query("select r from Restaurante r where nome like %:nome% and cozinha.id = :cozinhaId")
	List<Restaurante> consultarPorNomeECozinhaId(String nome, Long cozinhaId);
	
	//	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
	
	Optional<Restaurante> getFirstByNomeContaining(String nome);
	
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	Long countByCozinhaId(Long id);
	
}

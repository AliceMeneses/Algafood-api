package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Restaurante> buscarPorNomeETaxaFreteEntre(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {

		var jpql = "select r from Restaurante r where r.nome like :nome and r.taxaFrete "
				+ "between :taxaFreteInicial and :taxaFreteFinal";

		return entityManager.createQuery(jpql, Restaurante.class)
				.setParameter("nome", "%" + nome + "%")
				.setParameter("taxaFreteInicial", taxaFreteInicial)
				.setParameter("taxaFreteFinal", taxaFreteFinal)
				.getResultList();

	}

}

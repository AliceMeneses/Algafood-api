package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Restaurante> criteriaQuery = builder.createQuery(Restaurante.class);
		Root<Restaurante> root = criteriaQuery.from(Restaurante.class);
		
		Predicate nomePredicate = builder.like(root.get("nome"), "%" + nome + "%");
		Predicate taxaFreteInicialPredicate = builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial);
		Predicate taxaFreteFinalPredicate = builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal);

		criteriaQuery.where(nomePredicate, taxaFreteInicialPredicate, taxaFreteFinalPredicate);
		
		TypedQuery<Restaurante> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}

}

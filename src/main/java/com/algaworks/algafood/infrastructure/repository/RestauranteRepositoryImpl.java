package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Restaurante> buscarPorNomeETaxaFreteEntre(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {

		var jpql = new StringBuilder();
		jpql.append("select r from Restaurante r where 0=0 ");
		
		Map<String, Object> parametros = new HashMap<>();
		
		if (StringUtils.hasLength(nome)) {
			jpql.append("and r.nome like :nome");
			parametros.put("nome", "%"+nome+"%");
		}
		
		if (taxaFreteInicial != null) {
			jpql.append(" and r.taxaFrete >= :taxaFreteInicial");
			parametros.put("taxaFreteInicial", taxaFreteInicial);
		}
		
		if (taxaFreteFinal != null) {
			jpql.append(" and r.taxaFrete <= :taxaFreteFinal");
			parametros.put("taxaFreteFinal", taxaFreteFinal);
		}

		TypedQuery<Restaurante> query = entityManager.createQuery(jpql.toString(), Restaurante.class);

		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
		
		return query.getResultList();
	}

}

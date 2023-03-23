package com.algaworks.algafood.infrastructure.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> 
	implements CustomJpaRepository<T, ID> {
	
	@Autowired
	private EntityManager entityManager;

	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public Optional<T> buscarPrimeiro() {
		var jpql = "from " + getDomainClass().getName();
		
		T entidade = entityManager.createQuery(jpql, getDomainClass())
				.setMaxResults(1)
				.getSingleResult();
		
		return Optional.ofNullable(entidade);
	}

	@Override
	public void detach(T entity) {
		entityManager.detach(entity);
	}

}

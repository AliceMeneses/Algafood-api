package com.algaworks.algafood.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Cozinha> listar(){
		return entityManager.createQuery("select c from Cozinha c", Cozinha.class).getResultList();
	}
	
	@Transactional
	public Cozinha adicionar(Cozinha cozinha) {
		return entityManager.merge(cozinha);
	}
	
}

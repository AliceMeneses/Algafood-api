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
	
	public Cozinha buscar(Long id) {
		return entityManager.find(Cozinha.class, id);
	}
	
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return entityManager.merge(cozinha);
	}
	
	@Transactional
	public void excluir(Cozinha cozinha) {
		cozinha = buscar(cozinha.getId());
		entityManager.remove(cozinha);
	}
	
}

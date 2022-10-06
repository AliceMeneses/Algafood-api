package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Cozinha> listar(){
		return entityManager.createQuery("select c from Cozinha c", Cozinha.class).getResultList();
	}
	
	@Override
	public Cozinha buscar(Long id) {
		return entityManager.find(Cozinha.class, id);
	}
	
	@Override
	public List<Cozinha> buscarPorNome(String nome) {
		return entityManager.createQuery("select c from Cozinha c where nome like :nome", Cozinha.class)
			.setParameter("nome", "%"+ nome + "%").getResultList();
	}
	
	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {
		return entityManager.merge(cozinha);
	}
	
	@Transactional
	@Override
	public void remover(Long id) {
		Cozinha cozinha = buscar(id);
		
		if(cozinha == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		entityManager.remove(cozinha);
	}

}

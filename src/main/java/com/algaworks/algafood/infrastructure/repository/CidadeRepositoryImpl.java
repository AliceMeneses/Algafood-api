package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Component
public class CidadeRepositoryImpl implements CidadeRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Cidade> listar(){
		return entityManager.createQuery("select c from Cidade c", Cidade.class).getResultList();
	}
	
	@Override
	public Cidade buscar(Long id) {
		return entityManager.find(Cidade.class, id);
	}
	
	@Transactional
	@Override
	public Cidade salvar(Cidade cidade) {
		return entityManager.merge(cidade);
	}
	
	@Transactional
	@Override
	public void remover(Cidade cidade) {
		cidade = buscar(cidade.getId());
		entityManager.remove(cidade);
	}
	
}

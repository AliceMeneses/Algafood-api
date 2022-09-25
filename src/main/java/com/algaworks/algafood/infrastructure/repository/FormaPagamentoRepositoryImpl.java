package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Component
public class FormaPagamentoRepositoryImpl implements FormaPagamentoRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<FormaPagamento> listar(){
		return entityManager.createQuery("select f from FormaPagamento f", FormaPagamento.class).getResultList();
	}
	
	@Override
	public FormaPagamento buscar(Long id) {
		return entityManager.find(FormaPagamento.class, id);
	}
	
	@Transactional
	@Override
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return entityManager.merge(formaPagamento);
	}
	
	@Transactional
	@Override
	public void remover(FormaPagamento formaPagamento) {
		formaPagamento = buscar(formaPagamento.getId());
		entityManager.remove(formaPagamento);
	}
	
}

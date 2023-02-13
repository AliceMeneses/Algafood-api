package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		restaurante.setCozinha(cozinha);
		
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		restaurante.getEndereco().setCidade(cidade);
		
		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long id) {
		Restaurante restaurante = buscarOuFalhar(id);
		
		restaurante.ativar();
	}
	
	@Transactional
	public void inativar(Long id) {
		Restaurante restaurante = buscarOuFalhar(id);
		
		restaurante.inativar();
	}
	
	public Restaurante buscarOuFalhar(Long id) {
		return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(id));
	}
	
	public Restaurante buscarPorIdECarregarCidadeOuFalhar(Long id) {
		return restauranteRepository.consultarPorIdECarregarCidade(id).orElseThrow(() -> new RestauranteNaoEncontradoException(id));

	}
}

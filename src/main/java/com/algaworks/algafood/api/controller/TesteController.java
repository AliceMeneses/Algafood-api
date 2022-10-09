package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@GetMapping("/cozinhas")
	public List<Cozinha> buscarPorNome(@RequestParam String nome) {
		return cozinhaRepository.findByNomeContaining(nome);
	}

	@GetMapping("/restaurantes")
	public List<Restaurante> buscarPorTaxaFreteEntre(@RequestParam BigDecimal taxaFreteInicial,
			@RequestParam BigDecimal taxaFreteFinal) {
		return restauranteRepository.findByTaxaFreteBetween(taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/restaurantes/por-nome-e-cozinha-id")
	public List<Restaurante> buscarPorNomeECozinhaId(@RequestParam String nome, @RequestParam Long cozinhaId) {
		return restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId);
	}
	
	@GetMapping("/restaurantes/primeiro-por-nome")
	public Optional<Restaurante> buscarPrimeiroPorNome(@RequestParam String nome) {
		return restauranteRepository.getFirstByNomeContaining(nome);
	}
	
	
	@GetMapping("/restaurantes/top-2-por-nome")
	public List<Restaurante> buscarTop2PorNome(@RequestParam String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
}

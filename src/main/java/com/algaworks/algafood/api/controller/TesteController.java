package com.algaworks.algafood.api.controller;

import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.specification.RestauranteSpecification.comNomeSemelhante;

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
	
	@GetMapping("/cozinhas/existe-por-nome")
	public Boolean cozinhaExistePorNome(@RequestParam String nome) {
		return cozinhaRepository.existsByNome(nome);
	}

	@GetMapping("/restaurantes")
	public List<Restaurante> buscarPorTaxaFreteEntre(@RequestParam BigDecimal taxaFreteInicial,
			@RequestParam BigDecimal taxaFreteFinal) {
		return restauranteRepository.findByTaxaFreteBetween(taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/restaurantes/por-nome-e-cozinha-id")
	public List<Restaurante> buscarPorNomeECozinhaId(@RequestParam String nome, @RequestParam Long cozinhaId) {
		return restauranteRepository.consultarPorNomeECozinhaId(nome, cozinhaId);
	}
	
	@GetMapping("/restaurantes/primeiro-por-nome")
	public Optional<Restaurante> buscarPrimeiroPorNome(@RequestParam String nome) {
		return restauranteRepository.getFirstByNomeContaining(nome);
	}
	
	
	@GetMapping("/restaurantes/top-2-por-nome")
	public List<Restaurante> buscarTop2PorNome(@RequestParam String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
	@GetMapping("/restaurantes/quantidade-por-cozinha")
	public Long buscarQuantidadeRestaurantesPorCozinha(@RequestParam Long id) {
		return restauranteRepository.countByCozinhaId(id);
	}
	
	@GetMapping("/restaurantes/por-nome-e-frete")
	public List<Restaurante> buscarPorNomeETaxaFreteEntre(@RequestParam(required = false) String nome,
			@RequestParam(required = false) BigDecimal taxaFreteInicial, @RequestParam(required = false) BigDecimal taxaFreteFinal) {
		return restauranteRepository.buscarPorNomeETaxaFreteEntre(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/restaurantes/por-nome-e-com-frete-gratis")
	public List<Restaurante> buscarPorNomeEComFreteGratis(@RequestParam String nome) {
		
		return restauranteRepository.findAll(comNomeSemelhante(nome).and(comFreteGratis()));
	}
	
}

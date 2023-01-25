package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import com.algaworks.algafood.util.DatabaseCleaner;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@BeforeEach
	private void setUp() {
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	@Test
	void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		
		//cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		//ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		//validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		assertThrows(ConstraintViolationException.class, () -> cadastroCozinha.salvar(novaCozinha));
	}

	@Test
	void deveFalhar_QuandoExcluirCozinhaEmUso() {
		assertThrows(EntidadeEmUsoException.class, () -> cadastroCozinha.remover(1L));
	}
	
	@Test
	void deveFalhar_QuandoExcluirCozinhaInexistente() {
		assertThrows(CozinhaNaoEncontradaException.class, () -> cadastroCozinha.remover(18L));
	}
	
	private void prepararDados() {
		Cozinha cozinha = new Cozinha();
		cozinha.setNome("Indiana");
		cozinha = cozinhaRepository.save(cozinha);
		
		Restaurante restaurante = new Restaurante();
		restaurante.setCozinha(cozinha);
		restaurante.setNome("Tulsi Indian Cuisine");
		restaurante.setTaxaFrete(new BigDecimal("5"));
		restauranteRepository.save(restaurante);
		
	}
}

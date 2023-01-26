package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaAT {

	private static final int COZINHA_ID_INEXISTENTE = 100;

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository repository;
	
	private Cozinha cozinhaTailandesa;
	private int quantidadeCozinhasCadastradas;
	private String jsonCozinhaChinesa;
	
	@BeforeEach
	private void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		databaseCleaner.clearTables();
		prepararDados(); 
		
		jsonCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
	}

	@Test
	void deveRetornarStatus200_QuandoConsultarCozinhas() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeCozinhasCadastradas));
	}
	
	@Test
	void deveRetornarStatus201_QuandoCadastrarCozinha() {
		
		given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(jsonCozinhaChinesa)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		
		given()
			.accept(ContentType.JSON)
			.pathParam("cozinhaId", cozinhaTailandesa.getId())
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo("Tailandesa"));
	}
	
	@Test
	void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		
		given()
			.accept(ContentType.JSON)
			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinhaIndiana = new Cozinha();
		cozinhaIndiana.setNome("Indiana");
		repository.save(cozinhaIndiana);
		
		cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		cozinhaTailandesa = repository.save(cozinhaTailandesa);
		
	    quantidadeCozinhasCadastradas = (int) repository.count();
	}
	
}

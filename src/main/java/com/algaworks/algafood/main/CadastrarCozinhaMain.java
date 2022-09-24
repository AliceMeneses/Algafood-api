package com.algaworks.algafood.main;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

public class CadastrarCozinhaMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinhaBrasileira = new Cozinha();
		cozinhaBrasileira.setNome("Brasileira");
		
		Cozinha cozinhaJaponesa = new Cozinha();
		cozinhaJaponesa.setNome("Japonesa");
		
		cozinhaBrasileira = cozinhaRepository.salvar(cozinhaBrasileira);
		cozinhaJaponesa = cozinhaRepository.salvar(cozinhaJaponesa);
		
		System.out.printf("%d - %s\n", cozinhaBrasileira.getId(), cozinhaBrasileira.getNome());
		System.out.printf("%d - %s\n", cozinhaJaponesa.getId(), cozinhaJaponesa.getNome());
	}
}

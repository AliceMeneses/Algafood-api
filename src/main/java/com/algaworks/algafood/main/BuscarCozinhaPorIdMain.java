package com.algaworks.algafood.main;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.dao.CozinhaDao;
import com.algaworks.algafood.domain.model.Cozinha;

public class BuscarCozinhaPorIdMain {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE).run(args);

		CozinhaDao cozinhaDao = applicationContext.getBean(CozinhaDao.class);

		Cozinha cozinha = cozinhaDao.buscar(1L);

		System.out.println(cozinha.getNome());
	}
}

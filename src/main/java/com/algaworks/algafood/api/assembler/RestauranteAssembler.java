package com.algaworks.algafood.api.assembler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	@PersistenceContext
	private EntityManager manager;
	
    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }
    
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
    	manager.detach(restaurante.getCozinha());
    	modelMapper.map(restauranteInput, restaurante);
    }
}

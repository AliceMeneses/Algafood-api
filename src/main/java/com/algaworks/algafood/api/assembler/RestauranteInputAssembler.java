package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public RestauranteInput toModelInputObject(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteInput.class);
	}
	
}

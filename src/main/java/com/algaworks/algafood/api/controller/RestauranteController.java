package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteAssembler;
import com.algaworks.algafood.api.assembler.RestauranteInputAssembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ValidacaoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository repository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private SmartValidator validator;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteAssembler restauranteAssembler;
	
	@Autowired
	private RestauranteInputAssembler restauranteInputAssembler;

	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(repository.findAll());
	}

	@GetMapping("/{id}")
	public RestauranteModel buscar(@PathVariable Long id) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(id);
		return restauranteModelAssembler.toModel(restaurante);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel salvar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			
			Restaurante restaurante = restauranteAssembler.toDomainObject(restauranteInput);
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{id}")
	public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
	    
	    try {
			Restaurante restauranteAtual = cadastroRestaurante.buscarPorIdECarregarCidadeOuFalhar(id);
			restauranteAssembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
		    return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
	    } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
	    	throw new NegocioException(e.getMessage(), e);
		}
	}

	@PatchMapping("/{id}")
	public RestauranteModel atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos, HttpServletRequest servletRequest) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(id);
	    
	    merge(campos, restauranteAtual, servletRequest);
	    
	    validate(restauranteAtual, "restaurante");
	    
	    RestauranteInput restauranteInput = restauranteInputAssembler.toModelInputObject(restauranteAtual);
	    return atualizar(id, restauranteInput);
	}

	private void validate(Restaurante restaurante, String nome) {
		
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, nome);
		validator.validate(restaurante, bindingResult);
				
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest servletRequest) {

		ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(servletRequest);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException ex) {
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			throw new HttpMessageNotReadableException(rootCause.getMessage(), rootCause, servletServerHttpRequest);
		}
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long id) {
		cadastroRestaurante.ativar(id);
	}
	
	@DeleteMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long id) {
		cadastroRestaurante.inativar(id);
	}
	
	@PutMapping("{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@PutMapping("{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
	}
	
}

package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.api.model.input.UsuarioSemSenhaInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;
	
	@GetMapping
	private List<UsuarioModel> listar() {
		List<Usuario> usuarios = cadastroUsuario.listar();
		return usuarioModelAssembler.toCollectionModel(usuarios);
	}
	
	@GetMapping("/{id}")
	public UsuarioModel buscar(@PathVariable Long id) {
		Usuario usuario = cadastroUsuario.buscar(id);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@RequestBody UsuarioInput usuarioInput) {
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
		usuario = cadastroUsuario.salvar(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PutMapping("/{id}")
	public UsuarioModel atualizar(@PathVariable Long id, @RequestBody UsuarioSemSenhaInput usuarioSemSenhaInput) {
		Usuario usuario = cadastroUsuario.buscar(id);
		usuarioInputDisassembler.copyToDomainObject(usuarioSemSenhaInput, usuario);
		usuario = cadastroUsuario.salvar(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PutMapping("/{id}/senha")
	public UsuarioModel atualizarSenha(@PathVariable Long id, @RequestBody UsuarioSenhaInput usuarioInput) {
		Usuario usuario = cadastroUsuario.buscar(id);
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuario);
		usuario = cadastroUsuario.salvar(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		cadastroUsuario.remover(id);
	}
	
}

package com.zk.minhasfinancas.service;

import java.util.Optional;

import com.zk.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario user);

	void validarEmail(String email);

	Optional<Usuario> BuscarById(Long id);

}

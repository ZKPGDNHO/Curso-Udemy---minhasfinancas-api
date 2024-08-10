package com.zk.minhasfinancas.service;

import com.zk.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario user);
	
	void validarEmail(String email);
		
	
	
}

package com.zk.minhasfinancas.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.minhasfinancas.exception.ErroAutenticacao;
import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Usuario;
import com.zk.minhasfinancas.model.repository.UsuarioRepository;

import com.zk.minhasfinancas.service.UsuarioService;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> user = repository.findByEmail(email);
		if (!user.isPresent()) {
			throw new ErroAutenticacao("usuario nao encontrado");
		}
		if (!user.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha invalida");
		}
		return user.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario user) {
		validarEmail(user.getEmail());
		return repository.save(user);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("email invalido");
		}

	}

	@Override
	public Optional<Usuario> BuscarById(Long id) {
		return repository.findById(id);
	}

}

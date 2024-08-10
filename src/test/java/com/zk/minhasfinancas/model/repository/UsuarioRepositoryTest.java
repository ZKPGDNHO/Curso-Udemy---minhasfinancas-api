package com.zk.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.zk.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	@Autowired
	TestEntityManager entityManager;

	@Test
	public void EmailExisteTeste() {
		Usuario usu1 = new Usuario(null, "usuario", "usuario@email.com", "a");
		entityManager.persist(usu1);

		boolean result = repository.existsByEmail("usuario@email.com");

		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void VerificaEmailTeste() {

		boolean result = repository.existsByEmail("usuario@email.com");

		Assertions.assertThat(result).isFalse();

	}

	@Test
	public void RepositorySaveTest() {
		Usuario user = new Usuario(null, "user", "user@email.com", "123");
		Usuario SavedUser = repository.save(user);

		Assertions.assertThat(SavedUser.getId()).isNotNull();
	}

	@Test
	public void BuscaUsuarioByEmail() {
		Usuario user = new Usuario(null, "user", "user@email.com", "123");
		entityManager.persist(user);

		Optional<Usuario> result = repository.findByEmail("user@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	@Test
	public void RetornaVazioSeBuscarUsuarioByEmailQuandoNaoExisteNaBase() {

		Optional<Usuario> result = repository.findByEmail("user@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();
	}
}
package com.zk.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.zk.minhasfinancas.exception.ErroAutenticacao;
import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Usuario;
import com.zk.minhasfinancas.model.repository.UsuarioRepository;
import com.zk.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;
    UsuarioRepository repository;

    @Before
    public void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(repository);
    }

    @Test(expected = Test.None.class)
    public void SalvarUsuarioTest() {
        Usuario user = new Usuario(1l, "user", "email@email.com", "123");

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(user);

        Usuario usuarioSalvo = service.salvarUsuario(user);

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("user");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("123");

    }

    /*
     * @Test resolvo despues ;D
     * public void NaoDeveSalvarUsuarioSeEmailCadastrado() {
     * String email = "email@email.com";
     * Usuario usuario = new Usuario(null, null, email, null);
     * 
     * Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email
     * );
     * 
     * service.salvarUsuario(usuario);
     * 
     * Mockito.verify(repository, Mockito.never()).save(usuario);
     * }
     */
    @Test(expected = Test.None.class)
    public void AutenticarUsuariotest() {
        String email = "email@email.com";
        String senha = "123";

        Usuario user = new Usuario(1l, "user", email, senha);
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void ErroSeNaoEncontrarUsuarioCadastrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable excep = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "xuletera"));
        Assertions.assertThat(excep)
                .isInstanceOf(ErroAutenticacao.class)
                .hasMessage("usuario nao encontrado");
    }

    @Test
    public void RetornaErroQuandoSenhaIncorreta() {
        String senha = "123";
        Usuario user = new Usuario(1l, "Mr.Wando", "email@email.com", senha);
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        Throwable excep = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "xuletera"));
        Assertions.assertThat(excep)
                .isInstanceOf(ErroAutenticacao.class)
                .hasMessage("Senha invalida");
    }

    @Test(expected = Test.None.class)
    public void ValidaEmail() {

        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail("Emailqualquer123@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void ConferirEmailCadastrado() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        service.validarEmail("user@email.com");
    }
}

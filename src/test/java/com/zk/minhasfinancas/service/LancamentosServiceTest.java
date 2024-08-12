package com.zk.minhasfinancas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.zk.minhasfinancas.exception.ErroAutenticacao;
import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;
import com.zk.minhasfinancas.model.entity.TipoLancamento;
import com.zk.minhasfinancas.model.entity.Usuario;
import com.zk.minhasfinancas.model.repository.LancamentoRepository;
import com.zk.minhasfinancas.model.repository.UsuarioRepository;
import com.zk.minhasfinancas.service.impl.LancamentosServiceImpl;
import com.zk.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentosServiceTest {

    LancamentosServiceImpl service;

    LancamentoRepository repository;

    @Before
    public void setUp() {
        // Crie manualmente o mock do repositório
        repository = Mockito.mock(LancamentoRepository.class);

        // Crie manualmente o spy do serviço, injetando o repositório mockado
        service = Mockito.spy(new LancamentosServiceImpl(repository));

        // Se houver outras dependências, injete-as aqui
    }

    @Test
    public void deveSalvarLancamento() {

        Lancamentos lanASalvar = new Lancamentos(1l, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        Lancamentos lanSalvo = new Lancamentos(2l, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validate(lanASalvar);

        lanSalvo.setId(1l);
        lanSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lanASalvar)).thenReturn(lanSalvo);

        Lancamentos l = service.salvar(lanASalvar);

        Assertions.assertThat(l.getId()).isEqualTo(lanSalvo.getId());
        Assertions.assertThat(l.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void NaoSalvaLancamentoSeErroValidacao() {

        Lancamentos lanASalvar = new Lancamentos(1l, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        Mockito.doThrow(RegraNegocioException.class).when(service).validate(lanASalvar);

        Assertions.catchThrowableOfType(() -> service.salvar(lanASalvar), RegraNegocioException.class);

        Mockito.verify(repository, Mockito.never()).save(lanASalvar);
    }

    @Test
    public void deveAtualizarLancamento() {

        Lancamentos lanSalvo = new Lancamentos(2l, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        lanSalvo.setId(1l);
        lanSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validate(lanSalvo);

        Mockito.when(repository.save(lanSalvo)).thenReturn(lanSalvo);

        service.atualizar(lanSalvo);

        Mockito.verify(repository, Mockito.times(1)).save(lanSalvo);
    }

    @Test
    public void LancaErroSeAtualizarLancamentoQNaoFoiSalvo() {
        Usuario usu = new Usuario();
        Lancamentos lanASalvar = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        Assertions.catchThrowableOfType(() -> service.atualizar(lanASalvar), NullPointerException.class);

        Mockito.verify(repository, Mockito.never()).save(lanASalvar);

    }

    @Test
    public void DeletarTest() {
        Usuario usu = new Usuario();
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        l.setId(1l);
        service.delete(l);

        Mockito.verify(repository).delete(l);
    }

    @Test
    public void LancaErroSeDeletarLancamentoNaoSalvo() {

        Usuario usu = new Usuario();
        Lancamentos lanASalvar = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        Assertions.catchThrowableOfType(() -> service.delete(lanASalvar), NullPointerException.class);

        Mockito.verify(repository, Mockito.never()).delete(lanASalvar);

    }

    @Test
    public void FiltrarLancamento() {
        Usuario usu = new Usuario();
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        l.setId(1l);

        List<Lancamentos> list = Arrays.asList(l);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(list);

        List<Lancamentos> result = service.buscar(l);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(l);

    }

    @Test
    public void AtualizaStatusTest() {
        Usuario usu = new Usuario();
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        l.setId(1l);
        l.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento newStatus = StatusLancamento.EFETIVADO;

        Mockito.doReturn(l).when(service).atualizar(l);

        service.atualizarStatus(l, newStatus);

        Assertions.assertThat(l.getStatus()).isEqualTo(newStatus);
        Mockito.verify(service).atualizar(l);
    }

    @Test
    public void ObterLancamentoPorId() {
        Long id = 1l;
        Usuario usu = new Usuario();
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);
        l.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(l));

        Optional<Lancamentos> result = service.BuscarById(id);

        Assertions.assertThat(result.isPresent()).isTrue();

    }

    @Test
    public void RetornarVazioSeLancamentoPorIdNaoExiste() {
        Long id = 1l;
        Usuario usu = new Usuario();
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, usu,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);
        l.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Lancamentos> result = service.BuscarById(id);

        Assertions.assertThat(result.isPresent()).isFalse();

    }

    @Test
    public void DeveLancarErroValidarLancamento() {
        Lancamentos l = new Lancamentos();

        Throwable erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
                .hasMessage("Informe uma Descricao valida pls");

        l.setDescricao(" ");

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
                .hasMessage("Informe uma Descricao valida pls");

        l.setDescricao("dfnwpsksc");

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mes valido");

        l.setMes(0);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mes valido");

        l.setMes(13);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mes valido");

        l.setMes(4);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano valido");

        l.setAno(0);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano valido");

        l.setAno(195);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano valido");

        l.setAno(1234);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario");

        l.setUsuario(new Usuario());

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario");

        l.getUsuario().setId(1l);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor valido");

        l.setValor(BigDecimal.ZERO);

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor valido");

        l.setValor(BigDecimal.valueOf(1));

        erro = Assertions.catchThrowable(() -> service.validate(l));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class)
                .hasMessage("Informe um tipo de lancamento");

        l.setTipo(TipoLancamento.RECEITA);
        // pqp eu odeio fzr teste
    }

}

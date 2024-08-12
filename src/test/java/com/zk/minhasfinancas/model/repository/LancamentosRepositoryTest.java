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

import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;
import com.zk.minhasfinancas.model.entity.TipoLancamento;
import com.zk.minhasfinancas.model.entity.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentosRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void SalvarLancamentos() {
        Lancamentos l = new Lancamentos(1l, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        l = repository.save(l);

        Assertions.assertThat(l.getId()).isNotNull();
    }

    @Test
    public void DeletarLancamento() {

        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        entityManager.persist(l);

        l = entityManager.find(Lancamentos.class, l.getId());

        repository.delete(l);

        Lancamentos LanInexistente = entityManager.find(Lancamentos.class, l.getId());
        Assertions.assertThat(LanInexistente).isNull();

    }

    @Test
    public void deveAtualizarLancamento() {
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        entityManager.persist(l);

        l.setAno(2023);
        l.setDescricao("testando");
        l.setStatus(StatusLancamento.CANCELADO);

        repository.save(l);

        Lancamentos lanAtt = entityManager.find(Lancamentos.class, l.getId());

        Assertions.assertThat(lanAtt.getAno()).isEqualTo(2023);
        Assertions.assertThat(lanAtt.getDescricao()).isEqualTo("testando");
        Assertions.assertThat(lanAtt.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

    }

    @Test
    public void BuscarLancamentoPorID() {
        Lancamentos l = new Lancamentos(null, "lancamento X", 8, 2024, null,
                BigDecimal.valueOf(10), LocalDate.now(), TipoLancamento.RECEITA, StatusLancamento.PENDENTE);

        entityManager.persist(l);

        Optional<Lancamentos> lanEncontrado = repository.findById(l.getId());

        Assertions.assertThat(lanEncontrado.isPresent()).isTrue();

    }
}

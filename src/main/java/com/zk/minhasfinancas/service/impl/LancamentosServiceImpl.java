package com.zk.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;
import com.zk.minhasfinancas.model.repository.LancamentoRepository;
import com.zk.minhasfinancas.service.LancamentosService;

public class LancamentosServiceImpl implements LancamentosService {

    private LancamentoRepository repository;

    public LancamentosServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamentos salvar(Lancamentos l) {
        validate(l);
        l.setStatus(StatusLancamento.PENDENTE);
        return repository.save(l);
    }

    @Override
    @Transactional
    public Lancamentos atualizar(Lancamentos l) {
        Objects.requireNonNull(l.getId());
        validate(l);
        return repository.save(l);
    }

    @Override
    @Transactional
    public void delete(Lancamentos l) {
        Objects.requireNonNull(l.getId());
        repository.delete(l);
    }

    @Override
    @Transactional
    public List<Lancamentos> buscar(Lancamentos LancamentoFiltro) {
        Example example = Example.of(LancamentoFiltro, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING));

        return repository.findAll(example);

    }

    @Override
    public void atualizarStatus(Lancamentos l, StatusLancamento status) {
        l.setStatus(status);
        atualizar(l);
    }

    @Override
    public void validate(Lancamentos l) {

        if (l.getDescricao() == null || l.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descricao valida pls");
        }

        if (l.getMes() == null || l.getMes() < 1 || l.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mes valido");
        }

        if (l.getAno() == null || l.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um Ano valido");

        }

        if (l.getUsuario() == null || l.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuario");

        }

        if (l.getValor() == null || l.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um Valor valido");

        }

        if (l.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de lancamento");

        }
    }

}

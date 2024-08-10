package com.zk.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;

public interface LancamentosService {

    Lancamentos salvar(Lancamentos l);

    Lancamentos atualizar(Lancamentos l);

    void delete(Lancamentos l);

    List<Lancamentos> buscar(Lancamentos LancamentoFiltro);

    void atualizarStatus(Lancamentos l, StatusLancamento status);

    void validate(Lancamentos l);

    Optional<Lancamentos> BuscarById(Long id);

    BigDecimal obterSaldoByUsuario(Long id);
}

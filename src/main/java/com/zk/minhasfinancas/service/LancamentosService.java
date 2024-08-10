package com.zk.minhasfinancas.service;

import java.util.List;

import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;

public interface LancamentosService {

    Lancamentos salvar(Lancamentos l);

    Lancamentos atualizar(Lancamentos l);

    void delete(Lancamentos l);

    List<Lancamentos> buscar(Lancamentos LancamentoFiltro);

    void atualizarStatus(Lancamentos l, StatusLancamento status);

    void validate(Lancamentos l);
}

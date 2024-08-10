package com.zk.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {

    @Query(value = "select sum(l.valor) from Lancamentos l join l.usuario u where u.id = :idUsuario and l.tipo =:tipo group by u")
    BigDecimal obterSaldoByTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario,
            @Param("tipo") TipoLancamento tipo);

}

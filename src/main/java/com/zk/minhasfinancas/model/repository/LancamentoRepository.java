package com.zk.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zk.minhasfinancas.model.entity.Lancamentos;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {
	
}

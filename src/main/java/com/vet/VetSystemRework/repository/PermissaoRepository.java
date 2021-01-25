package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.VetSystemRework.domain.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

	List<Permissao> findAllByOrderByIdAsc();

	
}

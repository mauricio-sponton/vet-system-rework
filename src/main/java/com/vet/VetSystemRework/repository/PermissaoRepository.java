package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

//	List<Permissao> findAllByOrderByIdAsc();

	@Query("select p from Permissao p where not p.nome = 'ADMIN_WRITE' order by p.id ASC")
	List<Permissao> findTodasPermissoes();
	
}

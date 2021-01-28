package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>{

	@Query("select p from Perfil p where p.desc like :search%")
	Page<Perfil> findByName(String search, Pageable pageable);

	@Query("select p from Perfil p where not p.desc = 'ADMIN'")
	Page<Perfil> findAllPermissoes(Pageable pageable);


}

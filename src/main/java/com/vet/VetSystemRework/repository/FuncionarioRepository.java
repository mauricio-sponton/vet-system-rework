package com.vet.VetSystemRework.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	@Query("select f from Funcionario f where f.usuario.email = :email")
	Optional<Funcionario> findByUsuarioEmail(String email);

	@Query("select f from Funcionario f where f.usuario.id = :usuarioId")
	Optional<Funcionario> findByUsuarioId(Long usuarioId);

	
}

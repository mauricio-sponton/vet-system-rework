package com.vet.VetSystemRework.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	@Query("select f from Funcionario f where f.usuario.email = :email")
	Optional<Funcionario> findByUsuarioEmail(String email);

	@Query("select f from Funcionario f where f.usuario.id = :usuarioId")
	Optional<Funcionario> findByUsuarioId(Long usuarioId);

	@Query("select f from Funcionario f where exists(" + "select c from CargaHoraria c where f.nome = c.funcionario.nome and c.inicio <= :start and c.fim >= :end and c.diaDaSemana between :diaInicial and :diaFinal" + ") " 
			+ "and not exists(" + "select a from Agendamento a where (a.inicio <= :inicio and a.fim >= :fim)" + ") ")
	List<Funcionario> findFuncionariosByHorariosDisponiveis(LocalTime start, LocalTime end, int diaInicial,
			int diaFinal, LocalDateTime inicio, LocalDateTime fim);

	
}

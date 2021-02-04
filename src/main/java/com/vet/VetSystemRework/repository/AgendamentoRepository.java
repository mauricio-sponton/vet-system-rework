package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{

	@Query("select a from Agendamento a join a.funcionarios f where f.id = :id")
	List<Agendamento> findByFuncionarioId(Long id);

}

package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.CargaHoraria;

public interface CargaHorariaRepository extends JpaRepository<CargaHoraria, Long>{

	@Query("select c from CargaHoraria c where c.funcionario.id = :id")
	List<CargaHoraria> findHorarioByFuncionario(Long id);

}

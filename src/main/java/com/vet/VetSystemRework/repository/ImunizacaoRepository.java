package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Imunizacao;

public interface ImunizacaoRepository extends JpaRepository<Imunizacao, Long>{

	@Query("select i from Imunizacao i where i.descricao like :search% or i.especie.nome like :search%")
	Page<Imunizacao> findByName(String search, Pageable pageable);

	@Query("select i from Imunizacao i where i.descricao like :descricao and i.especie.nome like :especie")
	List<Imunizacao> findByNomeAndEspecie(String especie, String descricao);

	@Query("select i from Imunizacao i where i.especie.nome = :especie")
	List<Imunizacao> findVacinaByEspecie(String especie);
}

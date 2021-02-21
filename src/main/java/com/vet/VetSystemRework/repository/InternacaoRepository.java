package com.vet.VetSystemRework.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Internacao;

public interface InternacaoRepository extends JpaRepository<Internacao, Long> {

	@Query("select i from Internacao i where i.status like :search% OR i.animal.nome like :search%")
	Page<Internacao> findByStatusOrAnimal(String search, Pageable pageable);


}

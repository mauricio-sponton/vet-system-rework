package com.vet.VetSystemRework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.VetSystemRework.domain.HistoricoAnimal;
import com.vet.VetSystemRework.repository.HistoricoAnimalRepository;

@Service
public class HistoricoAnimalService {

	@Autowired
	private HistoricoAnimalRepository repository;

	@Transactional(readOnly = false)
	public void salvar(HistoricoAnimal historico) {
		repository.save(historico);
		
	}
	
	

}

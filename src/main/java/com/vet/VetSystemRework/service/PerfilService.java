package com.vet.VetSystemRework.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.repository.PerfilRepository;

@Service
public class PerfilService {

	@Autowired
	private PerfilRepository repository;

	@Transactional(readOnly = false)
	public void salvar(@Valid Perfil perfil) {
		repository.save(perfil);
		
	}


}

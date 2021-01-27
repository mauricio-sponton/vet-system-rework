package com.vet.VetSystemRework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vet.VetSystemRework.domain.Permissao;
import com.vet.VetSystemRework.repository.PermissaoRepository;

@Service
public class PermissaoService {

	@Autowired
	private PermissaoRepository repository;
	
	public List<Permissao> buscarTodasPermissoes() {
		return repository.findTodasPermissoes();
	}

	
}

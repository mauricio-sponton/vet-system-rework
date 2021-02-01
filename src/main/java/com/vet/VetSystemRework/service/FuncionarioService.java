package com.vet.VetSystemRework.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository repository;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid Funcionario funcionario) {
		repository.save(funcionario);
		
	}

	@Transactional(readOnly = true)
	public Funcionario buscarPorEmail(String email) {
		return repository.findByUsuarioEmail(email).orElse(new Funcionario());
	}

	@Transactional(readOnly = true)
	public Funcionario buscarPorIdUsuario(Long usuarioId) {
		return repository.findByUsuarioId(usuarioId).orElse(new Funcionario());
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
		
	}

}

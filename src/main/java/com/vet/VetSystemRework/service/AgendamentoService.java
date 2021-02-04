package com.vet.VetSystemRework.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.VetSystemRework.domain.Agendamento;
import com.vet.VetSystemRework.repository.AgendamentoRepository;



@Service
public class AgendamentoService {

	@Autowired
	private AgendamentoRepository repository;
	
	@Transactional(readOnly = false)
	public void salvar(@Valid Agendamento agendamento) {
		repository.save(agendamento);
	}

	@Transactional(readOnly = true)
	public List<Agendamento> buscarTodos() {
		return repository.findAll();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);	
	}

	@Transactional(readOnly = true)
	public List<Agendamento> buscarPorFuncionarioId(Long id) {
		return repository.findByFuncionarioId(id);
	}

	@Transactional(readOnly = true)
	public Agendamento buscarPorId(Long id) {
		return repository.findById(id).get();
	}

}

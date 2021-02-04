package com.vet.VetSystemRework.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

	@Transactional(readOnly = true)
	public Funcionario buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public List<Funcionario> buscarFuncionariosDisponiveis(LocalTime start, LocalTime end, int diaInicial, int diaFinal,
			LocalDateTime inicio, LocalDateTime fim) {
		return repository.findFuncionariosByHorariosDisponiveis(start, end, diaInicial, diaFinal, inicio, fim);
	}

}

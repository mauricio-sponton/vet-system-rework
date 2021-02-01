package com.vet.VetSystemRework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.vet.VetSystemRework.datatables.Datatables;
import com.vet.VetSystemRework.datatables.DatatablesColunas;
import com.vet.VetSystemRework.domain.Cliente;
import com.vet.VetSystemRework.repository.ClienteRepository;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Cliente cliente) {
		repository.save(cliente);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.CLIENTES);
		Page<Cliente> page = datatables.getSearch().isEmpty() ? repository.findAll(datatables.getPageable())
				: repository.findByNameOrEmail(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	public Cliente buscarPorId(Long id) {

		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);

	}

	public List<Cliente> buscarTodosClientes() {
		return repository.findAll();
	}

}
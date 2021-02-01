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
import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.repository.PerfilRepository;

@Service
public class PerfilService {

	@Autowired
	private PerfilRepository repository;
	
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(@Valid Perfil perfil) {
		repository.save(perfil);
		
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.PERFIS);
		Page<Perfil> page = datatables.getSearch().isEmpty() ? repository.findAllPermissoes(datatables.getPageable())
				: repository.findByName(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Perfil buscarPorId(Long id) {
		return repository.findById(id).get();
	}
	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);

	}

	@Transactional(readOnly = true)
	public List<Perfil> buscaTodosPerfisAtivos() {
		return repository.findAllPerfisAtivos();
	}

}

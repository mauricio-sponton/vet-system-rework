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
import com.vet.VetSystemRework.domain.Imunizacao;
import com.vet.VetSystemRework.repository.ImunizacaoRepository;

@Service
public class ImunizacaoService {

	@Autowired 
	private ImunizacaoRepository repository;
	
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvarImunizacao(@Valid Imunizacao imunizacao) {
		repository.save(imunizacao);
	}

	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.VACINAS);
		Page<Imunizacao> page =datatables.getSearch().isEmpty() ? repository.findAll(datatables.getPageable())
				: repository.findByName(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Imunizacao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		repository.deleteById(id);
	}
	
	public List<Imunizacao> buscarTodasImunizacoes(){
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Imunizacao> buscarImunizacoesPorNomeEEspecie(String especie, String descricao) {

		return repository.findByNomeAndEspecie(especie, descricao);
	}

	public List<Imunizacao> buscarTodasImunizacoesPorEspecie(String especie) {
		return repository.findVacinaByEspecie(especie);
	}

}

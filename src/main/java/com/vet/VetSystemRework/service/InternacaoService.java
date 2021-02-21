package com.vet.VetSystemRework.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.VetSystemRework.datatables.Datatables;
import com.vet.VetSystemRework.domain.Internacao;
import com.vet.VetSystemRework.repository.InternacaoRepository;

@Service
public class InternacaoService {

	@Autowired
	private InternacaoRepository repository;
	
	@Autowired
	private Datatables datatables;
	
//	@Transactional(readOnly = true)
//	public Map<String, Object> buscarTodos(HttpServletRequest request) {
//		datatables.setRequest(request);
//		datatables.setColunas(DatatablesColunas.INTERNACOES);
//		Page<Internacao> page = datatables.getSearch().isEmpty() ? repository.findAll(datatables.getPageable())
//				: repository.findByStatusOrAnimal(datatables.getSearch(), datatables.getPageable());
//		return datatables.getResponse(page);
//	}
//	
	public Page<Internacao> findPaginated(Pageable pageable) {
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Internacao> internacoes = repository.findAll();
		List<Internacao> lista = new ArrayList<Internacao>();
		
		if(internacoes.size() < startItem) {
			lista = Collections.emptyList();
		}else {
			int toIndex = Math.min(startItem + pageSize, internacoes.size());
            lista = internacoes.subList(startItem, toIndex);
		}
		Page<Internacao> internacaoPage
        = new PageImpl<Internacao>(lista, PageRequest.of(currentPage, pageSize), internacoes.size());
		return internacaoPage;
	}

	@Transactional(readOnly = true)
	public Internacao buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void salvarInternacao(@Valid Internacao internacao) {
		repository.save(internacao);
	}

}

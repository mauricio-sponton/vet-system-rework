package com.vet.VetSystemRework.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Especie;
import com.vet.VetSystemRework.domain.Imunizacao;
import com.vet.VetSystemRework.service.EspecieService;
import com.vet.VetSystemRework.service.ImunizacaoService;

@Controller
@RequestMapping("imunizacoes")
public class ImunizacaoController {

	@Autowired 
	private EspecieService especieService;
	
	@Autowired
	private ImunizacaoService service;
	
	@PostMapping("/salvar")
	public String salvarVacina(@Valid Imunizacao imunizacao, BindingResult result, RedirectAttributes attr, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "imunizacao/lista";
		}
		try {
			if(imunizacao.hasNotId()) {
				List<Imunizacao> vacinas = service.buscarImunizacoesPorNomeEEspecie(imunizacao.getEspecie().getNome(),imunizacao.getDescricao());
				if(vacinas.size() > 0) {
					attr.addFlashAttribute("falha", "Cadastro não realizado pois essa vacina já está cadastrada no sistema");
					return "redirect:/imunizacoes/listar";
				}
			}
			String mensagem = imunizacao.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso";
			service.salvarImunizacao(imunizacao);
			attr.addFlashAttribute("sucesso", mensagem);
		}catch(DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois essa vacina já está cadastrada no sistema");
		}
		
		return "redirect:/imunizacoes/listar";
	}
	@GetMapping("/listar")
	public String listarVacinas(Imunizacao imunizacao, @AuthenticationPrincipal UserDetails user) {
//		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.VETERINARIO.getDesc()))) {
//			return "vacina/lista";
//		}
//		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.SECRETARIA.getDesc()))) {
//			return "vacina/lista_sec";
		//}
		return "imunizacao/lista";
	}
	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarImunizacoesDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("imunizacao", service.buscarPorId(id));
		return "imunizacao/lista";
	}
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/imunizacoes/listar";
	}
	@ModelAttribute("especies")
	public List<Especie> listaDeEspecies(){
		return especieService.buscarTodasEspecies();
	}
}

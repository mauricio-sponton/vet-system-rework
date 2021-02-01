package com.vet.VetSystemRework.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Especie;
import com.vet.VetSystemRework.domain.Raca;
import com.vet.VetSystemRework.service.EspecieService;
import com.vet.VetSystemRework.service.RacaService;



@Controller
@RequestMapping("racas")
public class RacaController {
	
	@Autowired
	private RacaService service;
	
	@Autowired 
	private EspecieService especieService;

	@PostMapping("/salvar")
	public String salvarEspecie(@Valid Raca raca, BindingResult result, RedirectAttributes attr, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "raca/lista";
		}
		String titulo = raca.getEspecie().getNome();
		Especie especie = especieService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();
		
		if(raca.getEspecie() == null) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "raca/lista";
		}
		String mensagem = raca.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		try {
			raca.setEspecie(especie);
			service.salvarEspecie(raca);
			
			attr.addFlashAttribute("sucesso", mensagem);
			
		}catch(DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois essa raça já existe no sistema");
		}
		
		return "redirect:/racas/listar";
	}
	@GetMapping("/listar")
	public String listarRacas(Raca raca) {
		return "raca/lista";
	}
	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarRacasDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("raca", service.buscarPorId(id));
		return "raca/lista";
	}
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/racas/listar";
	}
	@ModelAttribute("especies")
	public List<Especie> listaDeEspecies(){
		return especieService.buscarTodasEspecies();
	}
	@GetMapping("/titulo")
	public ResponseEntity<?> getRacasPorTermo(@RequestParam("termo") String termo, @RequestParam("especie") String especie){
		List<String> racas = service.buscarRacaByTermo(termo, especie);
		return ResponseEntity.ok(racas);
	}
}

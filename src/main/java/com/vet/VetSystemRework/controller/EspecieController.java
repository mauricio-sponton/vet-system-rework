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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Especie;
import com.vet.VetSystemRework.service.EspecieService;

@Controller
@RequestMapping("especies")
public class EspecieController {

	@Autowired
	private EspecieService service;

	@PostMapping("/salvar")
	public String salvarEspecie(@Valid Especie especie, BindingResult result, RedirectAttributes attr, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "especie/lista";
		}
		String mensagem = especie.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		try {
			service.salvarEspecie(especie);
			attr.addFlashAttribute("sucesso", mensagem);
		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois essa espécie já existe no sistema");
		}

		return "redirect:/especies/listar";
	}

	@GetMapping("/listar")
	public String listarEspecies(Especie especie) {
		return "especie/lista";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarEspeciesDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("especie", service.buscarPorId(id));
		return "especie/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/especies/listar";
	}

	@GetMapping("/titulo")
	public ResponseEntity<?> getEspeciesPorTermo(@RequestParam("termo") String termo) {
		List<String> especies = service.buscarEspecieByTermo(termo);
		return ResponseEntity.ok(especies);
	}
}

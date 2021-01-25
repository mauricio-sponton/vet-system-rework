package com.vet.VetSystemRework.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.domain.Permissao;
import com.vet.VetSystemRework.service.PerfilService;
import com.vet.VetSystemRework.service.PermissaoService;

@Controller
@RequestMapping("perfis")
public class PerfilController {
	
	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private PerfilService service;

	@GetMapping("/listar")
	public String listarPerfis(Perfil perfil) {
		return "perfil/lista";
	}
	@ModelAttribute("permissoes")
	public List<Permissao> getPermissoes(){
		return permissaoService.buscarTodasPermissoes();
	}
	@PostMapping("/salvar")
	public String salvar(@Valid Perfil perfil, BindingResult result, RedirectAttributes attr, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/perfil/lista";
		}
		try {
			if (perfil.hasNotId()) {
				service.salvar(perfil);
				attr.addFlashAttribute("sucesso", "Perfil cadastrado com sucesso");
			} else {
				service.salvar(perfil);
				attr.addFlashAttribute("sucesso", "Dados alterados com sucesso");
			}
		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois perfil já existe");
		}

		return "redirect:/perfis/listar";
	}
}

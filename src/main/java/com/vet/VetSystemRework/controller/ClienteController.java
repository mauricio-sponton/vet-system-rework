package com.vet.VetSystemRework.controller;

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

import com.vet.VetSystemRework.domain.Cliente;
import com.vet.VetSystemRework.domain.UF;
import com.vet.VetSystemRework.service.ClienteService;

@Controller
@RequestMapping("clientes")
public class ClienteController {

	@Autowired
	private ClienteService service;

	@GetMapping("/listar")
	public String listar(Cliente cliente, @AuthenticationPrincipal UserDetails user) {
//		if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.CLIENTE_WRITE.getDesc()))) {
//			System.out.println("oioajdasoihfiasfhiasohf");
//		}
//		if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.CLIENTE_READ.getDesc()))) {
//			System.out.println("oioajdasoihfiasfhiasohf");
//		}
		return "cliente/lista";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarClientesDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/cliente/lista";
		}
		String mensagem = cliente.hasNotId() ? "Dados cadastrados com sucesso!" : "Dados alterados com sucesso!";
		try {
			service.salvar(cliente);
			attr.addFlashAttribute("sucesso", mensagem);
		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado pois o email ou cpf já existe");
		}

		return "redirect:/clientes/listar";
	}

	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", service.buscarPorId(id));
		return "cliente/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/clientes/listar";
	}

	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", service.buscarPorId(id));
		// model.addAttribute("animais", animalService.buscarAnimalPorDono(id));
		return "/cliente/visualizar";
	}

}

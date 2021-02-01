package com.vet.VetSystemRework.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.PermissaoTipo;
import com.vet.VetSystemRework.domain.Usuario;
import com.vet.VetSystemRework.service.FuncionarioService;
import com.vet.VetSystemRework.service.UsuarioService;


@Controller
public class HomeController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping({ "/home" })
	public String home(ModelMap model, @AuthenticationPrincipal UserDetails user, RedirectAttributes attr) {
		if(user.getAuthorities().stream().anyMatch(a -> !a.getAuthority().equals(PermissaoTipo.ADMIN_WRITE.getDesc()))) {
			Funcionario funcionario = funcionarioService.buscarPorEmail(user.getUsername());
			System.out.println(funcionario);
			if(funcionario.hasNotId()) {
				attr.addFlashAttribute("aviso",
						"Por favor preencha seus dados pessoais para continuar usando o sistema");
				return "redirect:/funcionarios/dados";
			}
		}
		return "home";
	}

	// abrir pagina login
	@GetMapping({ "/", "/login" })
	public String login() {

		return "login";
	}

	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha inválidos, tente novamente");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acesso negado");
		model.addAttribute("message", "Você não tem permissão para acesso a esta área ou ação");
		return "error";
	}
}


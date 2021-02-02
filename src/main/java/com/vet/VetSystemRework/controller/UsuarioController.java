package com.vet.VetSystemRework.controller;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.CargaHoraria;
import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.domain.Usuario;
import com.vet.VetSystemRework.service.CargaHorariaService;
import com.vet.VetSystemRework.service.FuncionarioService;
import com.vet.VetSystemRework.service.PerfilService;
import com.vet.VetSystemRework.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private CargaHorariaService cargaHorariaService;
	
	@GetMapping("/ajuda")
	public String abrirPaginaAjuda(Usuario usuario) {

		return "usuario/ajuda";
	}
	@GetMapping("/listar")
	public String listarUsuarios(Usuario usuario, Principal principal, @AuthenticationPrincipal UserDetails user) {
//		if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.ADMIN_WRITE.getDesc()))) {
//			System.out.println("oioajdasoihfiasfhiasohf");
//		}
		return "usuario/lista";
	}

	// listar na datatable
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	@ModelAttribute("ativos")
	public List<Perfil> getPerfis(){
		return perfilService.buscaTodosPerfisAtivos();
	}
	
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr,
			ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os campos");
			return "usuario/lista";
		}
		List<Perfil> perfis = usuario.getPerfis();
		System.out.println(perfis);
		if (perfis.size() > 2 ) {
			attr.addFlashAttribute("falha",
					"Não é possível cadastrar um usuário com mais de dois perfis.");
			attr.addFlashAttribute("usuario", usuario);
		} else if(perfis.size() == 2 && !perfis.contains(new Perfil(1L))){
			attr.addFlashAttribute("falha",
					"Um dos dois perfis deve ser ADMIN");
			attr.addFlashAttribute("usuario", usuario);
		}else {
			try {
				service.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Cadastro não realizado pois o email já existe");
			}
		}

		return "redirect:/u/listar";
	}
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {

		return new ModelAndView("usuario/lista", "usuario", service.buscarPorId(id));
	}

	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
			@RequestParam("senha3") String s3, Principal principal, RedirectAttributes attr) {
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente");
			return "redirect:/u/editar/senha";
		}
		Usuario u = service.buscarPorEmail(principal.getName());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente");
			return "redirect:/u/editar/senha";
		}
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso");
		return "redirect:/u/editar/senha";
	}
	@GetMapping("/visualizar/dados/{id}")
	public String visualizarDadosPessoais(@PathVariable("id") Long usuarioId, ModelMap modelmap) {
		Funcionario funcionario = funcionarioService.buscarPorIdUsuario(usuarioId);

		if (funcionario.hasNotId()) {		
			modelmap.addAttribute("status", 404);
			modelmap.addAttribute("error", "Página não encontrada");
			modelmap.addAttribute("message", "Os dados desse funcionário ainda não foram cadastrados");
			return "error";			
		}
		if(funcionario.hasId()) {
			List<CargaHoraria> horarios = cargaHorariaService.buscarHorarioPorFuncionario(funcionario.getId());
			modelmap.addAttribute("horarios", horarios);
			modelmap.addAttribute("funcionario", funcionario);
			return "funcionario/userview";
		}
		
		return "redirect:/u/lista";
	}
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr, @AuthenticationPrincipal UserDetails user) {
		Usuario usuario = service.buscarPorEmail(user.getUsername());
		if (usuario.getId() == id) {
			attr.addFlashAttribute("falha", "Você não pode deletar esse usuário");
			return "redirect:/u/listar";
		}
			Funcionario funcionario = funcionarioService.buscarPorIdUsuario(id);
			if(funcionario.hasId()) {
				funcionarioService.remover(funcionario.getId());
			}else {
				service.remover(id);
			}
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/u/listar";
	}
}

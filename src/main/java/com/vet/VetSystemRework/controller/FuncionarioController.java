package com.vet.VetSystemRework.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.Foto;
import com.vet.VetSystemRework.domain.Usuario;
import com.vet.VetSystemRework.service.FuncionarioService;
import com.vet.VetSystemRework.service.UsuarioService;
import com.vet.VetSystemRework.service.FotoService;

@Controller
@RequestMapping("funcionarios")
public class FuncionarioController {

	@Autowired
	private FotoService fotoService;

	@Autowired
	private FuncionarioService service;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/salvar")
	public String salvarDadosPessoais(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attr,
			ModelMap model, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails user) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Preencha os campos em vermelho!");
			return "funcionario/visualizar";
		}
		if (funcionario.hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			funcionario.setUsuario(usuario);
		}
		Foto foto = null;
		if (!file.isEmpty()) {
			foto = funcionario.getFoto().hasNotId() ? new Foto()
					: fotoService.buscarFotoId(funcionario.getFoto().getId());
			try {
				funcionario.setFoto(foto);
				fotoService.salvarFoto(file, foto);

			} catch (Exception e) {
				attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
			}

		}
		if (file.isEmpty() && funcionario.hasId()) {
			foto = funcionario.getFoto().hasId() ? fotoService.buscarFotoId(funcionario.getFoto().getId()) : null;

			if (foto == null) {
				funcionario.setFoto(null);
			} else {
				funcionario.setFoto(foto);
				fotoService.salvar(foto);
			}

		}

		service.salvar(funcionario);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		return "redirect:/funcionarios/dados";
	}

	@GetMapping("/dados")
	public String abrirPorUsuario(Funcionario funcionario, ModelMap model, @AuthenticationPrincipal UserDetails user) {
		funcionario = service.buscarPorEmail(user.getUsername());
		model.addAttribute("funcionario", funcionario);
		return "funcionario/visualizar";

	}
}

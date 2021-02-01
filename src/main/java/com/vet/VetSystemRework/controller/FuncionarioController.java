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
		if(funcionario.hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			funcionario.setUsuario(usuario);
		}
		if (!file.isEmpty()) {
			if (funcionario.getFoto().hasNotId()) {
				Foto foto = new Foto();
				try {
					fotoService.salvarFoto(file, foto);
					funcionario.setFoto(foto);
				} catch (Exception e) {
					attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
				}
			}
			if (funcionario.getFoto().hasId()) {
				Foto foto = fotoService.buscarFotoId(funcionario.getFoto().getId());
				foto.setFileName(file.getOriginalFilename());
				foto.setPath("/uploads/");
				
				try {
					fotoService.salvarFoto(file, foto);
					funcionario.setFoto(foto);
				} catch (Exception e) {
					attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
				}
			}

		}
		if (file.isEmpty() && funcionario.hasId()) {
			Foto foto = null;
			if(funcionario.getFoto().hasId()) {
				foto = fotoService.buscarFotoId(funcionario.getFoto().getId());
				funcionario.setFoto(foto);
				try {
					fotoService.salvar(foto);
				} catch (Exception e) {
					attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
				}
			}else {
				funcionario.setFoto(null);
				
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

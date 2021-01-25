package com.vet.VetSystemRework.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.domain.Permissao;
import com.vet.VetSystemRework.domain.Usuario;
import com.vet.VetSystemRework.service.PerfilService;
import com.vet.VetSystemRework.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private PerfilService perfilService;
	
	//@PreAuthorize("#usuario.perfis == 'ADMIN'")
	//@PreAuthorize("@securityService.hasAccess(1, #user)")
	@GetMapping("/listar")
	public String listarUsuarios(Usuario usuario, @AuthenticationPrincipal User user) {
		//Usuario usuario = service.buscarPorEmail(user.getUsername());
		return "usuario/lista";
	}

	// listar na datatable
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
}

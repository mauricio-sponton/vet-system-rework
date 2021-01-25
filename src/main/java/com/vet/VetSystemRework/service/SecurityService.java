package com.vet.VetSystemRework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.vet.VetSystemRework.domain.Perfil;
import com.vet.VetSystemRework.domain.Usuario;


@Service
public class SecurityService {
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PerfilService perfilService;
	
	//public void checaPerfis(@AuthenticationPrincipal User user) {
		//Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		//List<Perfil> perfis = perfilService.buscarPerfisPorUsuarioId(usuario.getId());
	//}
	//public boolean hasAccess(int codigo, @AuthenticationPrincipal User user) {
	//	return true;
	//}
}

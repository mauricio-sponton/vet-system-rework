package com.vet.VetSystemRework.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.CargaHoraria;
import com.vet.VetSystemRework.domain.CargaHorariaDTO;
import com.vet.VetSystemRework.domain.Foto;
import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.Usuario;
import com.vet.VetSystemRework.service.CargaHorariaService;
import com.vet.VetSystemRework.service.FotoService;
import com.vet.VetSystemRework.service.FuncionarioService;
import com.vet.VetSystemRework.service.UsuarioService;

@Controller
@RequestMapping("funcionarios")
public class FuncionarioController {

	@Autowired
	private FotoService fotoService;

	@Autowired
	private FuncionarioService service;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CargaHorariaService cargaHorariaService;

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

		if (funcionario.hasId()) {
			List<CargaHoraria> cargasVet = cargaHorariaService.buscarHorarioPorFuncionario(funcionario.getId());
			if (cargasVet.isEmpty()) {
				CargaHorariaDTO cargasForm = new CargaHorariaDTO();
				for (int i = 1; i <= 7; i++) {
					cargasForm.addCarga(new CargaHoraria());
				}
				model.addAttribute("form", cargasForm);
			}
			if (cargasVet.size() > 0) {
				List<CargaHoraria> listaEdicao = new ArrayList<>();
				cargaHorariaService.buscarHorarioPorFuncionario(funcionario.getId()).iterator()
						.forEachRemaining(listaEdicao::add);
				model.addAttribute("formEdit", new CargaHorariaDTO(listaEdicao));
			}
		}

		model.addAttribute("funcionario", funcionario);
		return "funcionario/visualizar";

	}

	@PostMapping("/salvar/horarios")
	public String salvarHorarios(@Valid @ModelAttribute CargaHorariaDTO form, BindingResult result, Model model,
			Funcionario funcionario, RedirectAttributes attr, @AuthenticationPrincipal UserDetails user) {
		int t = 1;
		funcionario = service.buscarPorEmail(user.getUsername());
		for (CargaHoraria c : form.getCargas()) {

			c.setFuncionario(funcionario);
			if (c.getInicio() == null && c.getFim() != null || c.getInicio() != null && c.getFim() == null) {
				attr.addFlashAttribute("falha", "Os horários dos dias trabalhados devem ser preenchidos.");
				return "redirect:/funcionarios/dados";
			}
			if (c.getInicio() != null && c.getFim() != null) {
				if (c.getInicio().isAfter(c.getFim()) || c.getInicio().equals(c.getFim())) {
					attr.addFlashAttribute("falha", "O horário de entrada não pode ser maior que o horário de saída.");
					return "redirect:/funcionarios/dados";
				}

			}
			switch (t) {
			case 1:
				c.setDiaDaSemana(Calendar.SUNDAY);
				break;
			case 2:
				c.setDiaDaSemana(Calendar.MONDAY);
				break;
			case 3:
				c.setDiaDaSemana(Calendar.TUESDAY);
				break;
			case 4:
				c.setDiaDaSemana(Calendar.WEDNESDAY);
				break;
			case 5:
				c.setDiaDaSemana(Calendar.THURSDAY);
				break;
			case 6:
				c.setDiaDaSemana(Calendar.FRIDAY);
				break;
			case 7:
				c.setDiaDaSemana(Calendar.SATURDAY);
				break;

			}
			t = t + 1;
			if (c.isAtivo() == true) {
				c.setFim(null);
				c.setInicio(null);
			}

		}
		cargaHorariaService.salvarTodos(form.getCargas());
		attr.addFlashAttribute("sucesso", "Dados cadastrados com sucesso");
		return "redirect:/funcionarios/dados";
	}
}

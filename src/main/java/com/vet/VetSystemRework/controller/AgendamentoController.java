package com.vet.VetSystemRework.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Agendamento;
import com.vet.VetSystemRework.domain.Animal;
import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.PermissaoTipo;
import com.vet.VetSystemRework.service.AgendamentoService;
import com.vet.VetSystemRework.service.AnimalService;
import com.vet.VetSystemRework.service.FuncionarioService;

@Controller
@RequestMapping("agenda")
public class AgendamentoController {

	@Autowired
	private AgendamentoService service;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AnimalService animalService;

//	@Autowired
//	private NotificacaoService notificacaoService;

	@GetMapping("/abrir")
	public String abrirAgenda(Agendamento agendamento, ModelMap model, @AuthenticationPrincipal User user,
			Funcionario funcionario) {
		return "agendamento/agenda";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Agendamento agendamento, BindingResult result, RedirectAttributes attr, ModelMap model,
			@AuthenticationPrincipal UserDetails user, @RequestParam("funcionario.id") String id) {
		System.out.println(id);
		
			
		if (result.hasErrors()
				|| (agendamento.getAnimal().getNome().isEmpty() && agendamento.getSem_cadastro().isEmpty())) {
			model.addAttribute("erro", "por favor preencha os campos");
			return "/agendamento/agenda";
		}
		if (agendamento.getInicio().isAfter(agendamento.getFim())
				|| agendamento.getInicio().isEqual(agendamento.getFim())) {
			model.addAttribute("erro", "A data de ínicio não pode ser igual ou ultrapassar a data de término");
			return "/agendamento/agenda";
		}

		try {
			if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.AGENDA_WRITE.getDesc()))) {
				List<Funcionario> funcionarios = new ArrayList<Funcionario>();
				Long funcId = Long.parseLong(id);
				Funcionario logado = funcionarioService.buscarPorEmail(user.getUsername());
				Funcionario avisado = funcionarioService.buscarPorId(funcId);
				funcionarios.add(logado);
				funcionarios.add(avisado);
				agendamento.setAvisado(avisado.getId());
				agendamento.setLogado(logado.getId());
				
				if (!agendamento.getAnimal().getNome().isEmpty()) {
					String titulo = agendamento.getAnimal().getNome();
					Animal animal = animalService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();
					agendamento.setSem_cadastro("");
					agendamento.setAnimal(animal);
				} else {
					agendamento.setAnimal(null);
				}

//				if (agendamento.hasNotId()) {
//
//					agendamento.setFuncionario(funcionarios);
					
//					Notificacao notificacao = new Notificacao();
//					LocalDate data = LocalDate.now();
//					notificacao.setData(data);
//					notificacao.setTitulo("Nova consulta agendada");
//					LocalTime horas = agendamento.getInicio().toLocalTime();
//					String paciente = null;
//					if (agendamento.getAnimal() != null) {
//						paciente = agendamento.getAnimal().getNome();
//					} else {
//						paciente = agendamento.getSem_cadastro();
//					}
//					notificacao.setDescricao("Uma nova consulta foi agendada para o dia "
//							+ agendamento.getInicio().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
//							+ " às " + horas + " ;" + "Nome do paciente: " + paciente + ";" + "Consulta agendada por: "
//							+ agendamento.getSecretaria().getNome());
//					v2.getNotificacoes().add(notificacao);

//					service.salvar(agendamento);
//
//					attr.addFlashAttribute("sucesso", "Agendamento cadastrado com sucesso");
//				} else {
//
					agendamento.setFuncionarios(funcionarios);
					service.salvar(agendamento);
					attr.addFlashAttribute("sucesso", "Dados alterados com sucesso");
				//}
			}
//
		} catch (DataIntegrityViolationException ex) {
			attr.addFlashAttribute("falha", "Cadastro não realizado");
		}

		return "redirect:/agenda/abrir";
	}

	@GetMapping("/todos")
	public @ResponseBody ArrayList<Map<String, Object>> getAgendamentos(Funcionario funcionario, @AuthenticationPrincipal UserDetails user) {
		List<Agendamento> events = null;
		if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.AGENDA_READ.getDesc()))) {
			events = service.buscarPorFuncionarioId(funcionario.getId());
		}
		if(user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(PermissaoTipo.AGENDA_WRITE.getDesc()))) {
			events = service.buscarTodos();
		}

		ArrayList<Map<String, Object>> allEvents = new ArrayList<Map<String, Object>>();
		if(events != null) {
			for (Agendamento agendamento : events) {
				Funcionario marcou = funcionarioService.buscarPorId(agendamento.getLogado());
				Funcionario avisado = funcionarioService.buscarPorId(agendamento.getAvisado());
				Map<String, Object> tudo = new LinkedHashMap<>();
				Map<String, Object> extend = new LinkedHashMap<>();
				tudo.put("id", agendamento.getId());
				tudo.put("title", agendamento.getTipo());
				tudo.put("start", agendamento.getInicio());
				tudo.put("end", agendamento.getFim());
				tudo.put("description", agendamento.getDescricao() != null ? agendamento.getDescricao() : "");
				tudo.put("backgroundColor", agendamento.getColor());
				extend.put("veterinario", avisado);

				extend.put("secretaria",
						marcou);
				if (agendamento.getAnimal() != null) {
					extend.put("cliente", agendamento.getAnimal().getCliente().getNome());
					extend.put("paciente",
							agendamento.getAnimal().getNome() != null ? agendamento.getAnimal().getNome() : "");
				} else {
					extend.put("pacienteNaoCadastrado",
							agendamento.getSem_cadastro() != null ? agendamento.getSem_cadastro() : "");
				}

				tudo.put("extendedProps", extend != null ? extend : "");
				allEvents.add(tudo);

			}
		}
		

		return allEvents;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		//Agendamento agendamento = service.buscarPorId(id);
//		LocalDate data = LocalDate.now();
//		LocalTime horas = agendamento.getInicio().toLocalTime();
//		String paciente = null;
//		
//		if (agendamento.getAnimal() != null) {
//			paciente = agendamento.getAnimal().getNome();
//		} else {
//			paciente = agendamento.getSem_cadastro();
//		}
//		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.SECRETARIA.getDesc()))) {
//			Veterinario v2 = veterinarioService.buscarPorId(agendamento.getVeterinario().getId());
//			Notificacao notificacao = new Notificacao();
//			notificacao.setData(data);
//			notificacao.setTitulo("Cancelamento de consulta");
//			notificacao.setDescricao("A consulta agendada para o dia "
//					+ agendamento.getInicio().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " às "
//					+ horas + " do " + paciente + " foi cancelada! " + ";" + "Cancelamento da consulta realizado por: "
//					+ agendamento.getSecretaria().getNome() + ";");
//			v2.getNotificacoes().add(notificacao);
//
//		}
//		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.VETERINARIO.getDesc()))) {
//			List<Secretaria> secretarias = secretariaService.buscarTodos();
//			Notificacao notificacao = new Notificacao();
//			notificacao.setData(data);
//			notificacao.setTitulo("Cancelamento de consulta");
//			notificacao.setDescricao("Consulta agendada para o dia "
//					+ agendamento.getInicio().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " às "
//					+ horas + ";" + "Paciente: " + paciente + ";" + "Agendada por: "
//					+ agendamento.getSecretaria().getNome() + ";" + "Cancelada por: "
//					+ agendamento.getVeterinario().getNome());
//
//			for (Secretaria s : secretarias) {
//				s.getNotificacoes().add(notificacao);
//			}
//			service.remover(id);
//			attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
//			return "redirect:/agenda/abrir";
//		}
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/agenda/abrir";
	}

}

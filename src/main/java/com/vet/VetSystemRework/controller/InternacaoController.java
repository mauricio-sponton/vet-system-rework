package com.vet.VetSystemRework.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Animal;
import com.vet.VetSystemRework.domain.Internacao;
import com.vet.VetSystemRework.service.AnimalService;
import com.vet.VetSystemRework.service.InternacaoService;

@Controller
@RequestMapping("internacoes")
public class InternacaoController {

	@Autowired
	private InternacaoService service;
	
	@Autowired
	private AnimalService animalService;

//	@GetMapping("/listar")
//	public String listar(Internacao internacao) {
//		return "internacao/lista";
//	}
//	@GetMapping("/datatables/server")
//	public ResponseEntity<?> listarInternacoesDatatables(HttpServletRequest request) {
//		return ResponseEntity.ok(service.buscarTodos(request));
//	}

	@GetMapping("/listar")
	public String listar(Internacao internacao, Model model, @RequestParam("pagina") Optional<Integer> pagina,
			@RequestParam("tamanho") Optional<Integer> tamanho) {

		int paginaAtual = pagina.orElse(1);
		int paginaTamanho = tamanho.orElse(2);

		Page<Internacao> internacaoPage = service.findPaginated(PageRequest.of(paginaAtual - 1, paginaTamanho));

		model.addAttribute("internacaoPage", internacaoPage);

		int totalPages = internacaoPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("paginaAtual", paginaAtual);
		return "internacao/lista";
	}

	@PostMapping("/salvar")
	public String salvarInternacao(@Valid Internacao internacao, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal UserDetails user, @RequestParam("status") String status, ModelMap model) {

		if (result.hasErrors() || internacao.getAnimal().getNome().isEmpty()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "internacao/lista";
		}
		
		if (internacao.hasId()) {
			Internacao int2 = service.buscarPorId(internacao.getId());
			if (int2.internacaoEncerrada() && status.equals("Ativa")) {
				attr.addFlashAttribute("falha", "Essa internação já foi encerrada, por favor cadastre uma nova");
				return "redirect:/internacoes/listar";
			}

		}
		String titulo = internacao.getAnimal().getNome();
		Animal animal = animalService.buscarPorTitulos(new String[] { titulo }).stream().findFirst().get();
		
		if(internacao.hasId() && animal.isAnimalInternado()) {
			attr.addFlashAttribute("falha", "O paciente já possui uma internação em andamento!");
			return "redirect:/internacoes/listar";
		}
		
		if(internacao.hasNotId() && status.equals("Encerrada")) {
			attr.addFlashAttribute("falha", "O paciente não possui nenhuma internação em andamento!");
			return "redirect:/internacoes/listar";
		}
		
		if(internacao.hasId() && status.equals("Encerrada")) {
			animal.setStatus("Normal");
		}
		
		internacao.setAnimal(animal);
		service.salvarInternacao(internacao);
		
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		return "redirect:/internacoes/listar";
	}
}

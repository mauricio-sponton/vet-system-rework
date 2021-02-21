package com.vet.VetSystemRework.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vet.VetSystemRework.domain.Animal;
import com.vet.VetSystemRework.domain.Cliente;
import com.vet.VetSystemRework.domain.Especie;
import com.vet.VetSystemRework.domain.Foto;
import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.HistoricoAnimal;
import com.vet.VetSystemRework.domain.HistoricoAnimalTipo;
import com.vet.VetSystemRework.domain.PermissaoTipo;
import com.vet.VetSystemRework.domain.Raca;
import com.vet.VetSystemRework.service.AnimalService;
import com.vet.VetSystemRework.service.ClienteService;
import com.vet.VetSystemRework.service.EspecieService;
import com.vet.VetSystemRework.service.FotoService;
import com.vet.VetSystemRework.service.FuncionarioService;
import com.vet.VetSystemRework.service.HistoricoAnimalService;
import com.vet.VetSystemRework.service.RacaService;
import com.vet.VetSystemRework.validacao.ValidacoesHistorico;

@Controller
@RequestMapping("pacientes")
public class AnimalController {

	@Autowired
	private EspecieService especieService;

	@Autowired
	private RacaService racaService;

	@Autowired
	private HistoricoAnimalService historicoAnimalService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AnimalService service;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private ClienteService clienteService;
	


	// Cadastrar o paciente
	@PostMapping("/salvar")
	public String salvarAnimal(@Valid Animal animal, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal UserDetails user, @RequestParam(required = false, value = "file") MultipartFile file, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "animal/lista";
		}

		// Pesquisas referente ao autocomplete
		String tituloEspecie = animal.getEspecie().getNome();
		String tituloRaca = animal.getRaca().getNome();
		Especie especie = especieService.buscarPorTitulos(new String[] { tituloEspecie }).stream().findFirst().get();
		Raca raca = racaService.buscarPorTitulos(new String[] { tituloRaca }).stream().findFirst().get();

		if (raca.getEspecie().getId() != especie.getId()) {
			attr.addFlashAttribute("falha", "Espécie e raça não condizem!");
			return "redirect:/pacientes/listar";
		}
		// -----------------------------------------------------------------------------------
		// file upload
		Foto foto = null;
		if (!file.isEmpty()) {
			foto = animal.getFoto().hasNotId() ? new Foto() : fotoService.buscarFotoId(animal.getFoto().getId());
			try {
				animal.setFoto(foto);
				fotoService.salvarFoto(file, foto);

			} catch (Exception e) {
				attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
			}

		}else {
			foto = animal.getFoto().hasNotId() ? null : fotoService.buscarFotoId(animal.getFoto().getId());
			
			if(foto != null) {
				animal.setFoto(foto);
			}
			
		}

		// -------------------------------------------------

		String mensagem = "";
		HistoricoAnimal historico = null;
		ValidacoesHistorico validacao = new ValidacoesHistorico();
		
			
			Funcionario funcionario = funcionarioService.buscarPorEmail(user.getUsername());
			String perfil = "";
			perfil = funcionario.getUsuario().getPerfis().size() > 1 ?
				funcionario.getUsuario().getPerfis().get(1).getDesc() 
				: funcionario.getUsuario().getPerfis().get(0).getDesc() ;
			
			// CADASTRO NO HISTÓRICO DO PACIENTE CASO ELE NÃO TENHA ID
			if (animal.hasNotId()) {
				mensagem = "Dados cadastrados com sucesso!";
				animal.setStatus("Normal");
				historico = validacao.novoCadastro(perfil, funcionario, animal);
				

			}
			// CADASTRO NO HISTÓRICO DO PACIENTE CASO ELE TENHA ID
			if (animal.hasId()) {
				mensagem = "Dados alterados com sucesso!";
				Animal status = service.buscarPorId(animal.getId());
				if (!status.getStatus().equals("Internado")) {
					animal.setStatus("Normal");
				} else {
					animal.setStatus("Internado");
				}
				historico = validacao.editarCadastro(animal, status, funcionario, perfil);
			}
		
		try {

			animal.setEspecie(especie);
			animal.setRaca(raca);
			service.salvarAnimal(animal);
			if(historico != null) {
				historicoAnimalService.salvar(historico);
			}

			attr.addFlashAttribute("sucesso", mensagem);
		} catch (Exception e) {
			attr.addFlashAttribute("falha", "Erro ao cadastrar o paciente!");
		}

		return "redirect:/pacientes/listar";

	}

	// ABRIR PÁGINA QUE LISTA OS PACIENTES
	@GetMapping("/listar")
	public String listarAnimais(Animal animal) {
		return "animal/lista";
	}

	// FUNÇÃO PARA CARREGAR A LISTA DE PACIENTES
	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarAnimaisDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}

	// FUNÇÃO PARA ATRIBUIR OS CLIENTES NO SELECT DO FORM
	@ModelAttribute("clientes")
	public List<Cliente> listaDeClientes() {
		return clienteService.buscarTodosClientes();
	}

	// EDITAR PACIENTES
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("animal", service.buscarPorId(id));
		return "animal/lista";
	}

	// EXCLUIR PACIENTES
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/pacientes/listar";
	}

	@GetMapping("/titulo")
	public ResponseEntity<?> getAnimaisPorTermo(@RequestParam("termo") String termo) {
		List<Animal> animais = service.buscarAnimaisByTermo(termo);
		return ResponseEntity.ok(animais);
	}
}

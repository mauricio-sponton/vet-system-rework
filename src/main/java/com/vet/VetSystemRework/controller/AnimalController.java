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

	@PostMapping("/salvar")
	public String salvarAnimal(@Valid Animal animal, BindingResult result, RedirectAttributes attr,
			@AuthenticationPrincipal UserDetails user, @RequestParam("file") MultipartFile file, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Por favor preencha os dados");
			return "animal/lista";
		}

		String tituloEspecie = animal.getEspecie().getNome();
		String tituloRaca = animal.getRaca().getNome();
		Especie especie = especieService.buscarPorTitulos(new String[] { tituloEspecie }).stream().findFirst().get();
		Raca raca = racaService.buscarPorTitulos(new String[] { tituloRaca }).stream().findFirst().get();

		if (raca.getEspecie().getId() != especie.getId()) {
			attr.addFlashAttribute("falha", "Espécie e raça não condizem!");
			return "redirect:/pacientes/listar";
		}

		Foto foto = null;
		if (!file.isEmpty()) {
			foto = animal.getFoto().hasNotId() ? new Foto() : fotoService.buscarFotoId(animal.getFoto().getId());
			try {
				animal.setFoto(foto);
				fotoService.salvarFoto(file, foto);

			} catch (Exception e) {
				attr.addFlashAttribute("falha", "Erro ao cadastrar foto!");
			}

		}
		if (file.isEmpty() && animal.hasId()) {
			foto = animal.getFoto().hasId() ? fotoService.buscarFotoId(animal.getFoto().getId()) : null;

			if (foto == null) {
				animal.setFoto(null);
			} else {
				animal.setFoto(foto);
				fotoService.salvar(foto);
			}

		}

		HistoricoAnimal historico = new HistoricoAnimal();
		LocalDate data = LocalDate.now();
		LocalTime hora = LocalTime.now();
		String mensagem = "";

		if (user.getAuthorities().stream()
				.anyMatch(a -> !a.getAuthority().equals(PermissaoTipo.ADMIN_WRITE.getDesc()))) {
			Funcionario funcionario = funcionarioService.buscarPorEmail(user.getUsername());
			String perfil = "";
			for (int i = 0; i <= funcionario.getUsuario().getPerfis().size(); i++) {
				perfil = funcionario.getUsuario().getPerfis().get(1).getDesc();
			}

			if (animal.hasNotId()) {
				mensagem = "Dados cadastrados com sucesso!";
				historico.setDescricao("O paciente foi cadastrado com sucesso!");
				historico.setTipo(HistoricoAnimalTipo.CADASTRO_NEW);
				historico.setUsuario(funcionario.getNome() + " (" + perfil + ")");
				animal.setStatus("Normal");

			}
			if (animal.hasId()) {
				mensagem = "Dados alterados com sucesso!";
				Animal status = service.buscarPorId(animal.getId());
				StringBuilder mud = new StringBuilder();

				if (!status.getNome().contains(animal.getNome())) {
					mud.append("O nome do paciente foi mudado de " + status.getNome() + " para " + animal.getNome()
							+ "." + ";");
					historico.setDescricao(mud.toString());
				}
				if (!status.getCliente().getNome().contains(animal.getCliente().getNome())) {
					mud.append("O dono do paciente foi mudado de " + status.getCliente().getNome() + " para "
							+ animal.getCliente().getNome() + "." + ";");
					historico.setDescricao(mud.toString());
				}
				if (!status.getRaca().getNome().contains(animal.getRaca().getNome())) {
					mud.append("A raça do paciente foi mudado de " + status.getRaca().getNome() + " para "
							+ animal.getRaca().getNome() + "." + ";");
					historico.setDescricao(mud.toString());
				}
				if (!status.getEspecie().getNome().contains(animal.getEspecie().getNome())) {
					mud.append("A espécie do paciente foi mudado de " + status.getEspecie().getNome() + " para "
							+ animal.getEspecie().getNome() + "." + ";");
					historico.setDescricao(mud.toString());
				}

				if (!status.getDataNascimento().equals(animal.getDataNascimento())) {
					mud.append("A data de nascimento do paciente foi mudado de "
							+ status.getDataNascimento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
							+ " para "
							+ animal.getDataNascimento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
							+ "." + ";");
					historico.setDescricao(mud.toString());
				}

				if (!status.getSexo().contains(animal.getSexo())) {
					mud.append("O sexo do paciente foi mudado de " + status.getSexo() + " para " + animal.getSexo()
							+ "." + ";");
					historico.setDescricao(mud.toString());
				}
				if (!status.getAlergias().contains(animal.getAlergias())) {
					mud.append("As alergias do paciente foram alteradas " + "." + ";");
					historico.setDescricao(mud.toString());
				}

				if (historico.getDescricao() != null) {
					historico.setTipo(HistoricoAnimalTipo.CADASTRO_EDIT);
					historico.setUsuario(funcionario.getNome() + " (" + perfil + ")");

				}
				if (!status.getStatus().equals("Internado")) {
					animal.setStatus("Normal");
				} else {
					animal.setStatus("Internado");
				}
			}
		}
		try {

			animal.setEspecie(especie);
			animal.setRaca(raca);
			service.salvarAnimal(animal);

			if (historico.getDescricao() != null) {
				historico.setData(data);
				historico.setHora(hora);
				historico.setAnimal(animal);
				historicoAnimalService.salvar(historico);
			}

			attr.addFlashAttribute("sucesso", mensagem);
		} catch (Exception e) {
			attr.addFlashAttribute("falha", "Erro ao cadastrar o paciente!");
		}

		return "redirect:/pacientes/listar";

	}
	@GetMapping("/listar")
	public String listarAnimais(Animal animal) {
		return "animal/lista";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> listarAnimaisDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	@ModelAttribute("clientes")
	public List<Cliente> listaDeClientes() {
		return clienteService.buscarTodosClientes();
	}
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("animal", service.buscarPorId(id));
		return "animal/lista";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/pacientes/listar";
	}
}

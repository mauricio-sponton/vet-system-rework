package com.vet.VetSystemRework.validacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vet.VetSystemRework.domain.Animal;
import com.vet.VetSystemRework.domain.Funcionario;
import com.vet.VetSystemRework.domain.HistoricoAnimal;
import com.vet.VetSystemRework.domain.HistoricoAnimalTipo;

public class ValidacoesHistorico {
	public HistoricoAnimal novoCadastro(String perfil, Funcionario funcionario, Animal animal) {
		HistoricoAnimal historico = new HistoricoAnimal();
		LocalDate data = LocalDate.now();
		LocalTime hora = LocalTime.now();
		historico.setDescricao("O paciente foi cadastrado com sucesso!");
		historico.setTipo(HistoricoAnimalTipo.CADASTRO_NEW);
		historico.setUsuario(funcionario.getNome() + " (" + perfil.substring(0, 1).toUpperCase()
				+ perfil.substring(1).toLowerCase() + ")");
		historico.setData(data);
		historico.setHora(hora);
		historico.setAnimal(animal);
		return historico;
	}

	public HistoricoAnimal editarCadastro(Animal animal, Animal status, Funcionario funcionario, String perfil) {
		HistoricoAnimal historico = new HistoricoAnimal();
		StringBuilder mud = new StringBuilder();
		LocalDate data = LocalDate.now();
		LocalTime hora = LocalTime.now();

		if (!status.getNome().equals(animal.getNome())) {
			mud.append(
					"O nome do paciente foi mudado de " + status.getNome() + " para " + animal.getNome() + "." + ";");
			historico.setDescricao(mud.toString());
		}
		if (!status.getCliente().getNome().equals(animal.getCliente().getNome())) {
			mud.append("O dono do paciente foi mudado de " + status.getCliente().getNome() + " para "
					+ animal.getCliente().getNome() + "." + ";");
			historico.setDescricao(mud.toString());
		}
		if (!status.getRaca().getNome().equals(animal.getRaca().getNome())) {
			mud.append("A raça do paciente foi mudado de " + status.getRaca().getNome() + " para "
					+ animal.getRaca().getNome() + "." + ";");
			historico.setDescricao(mud.toString());
		}
		if (!status.getEspecie().getNome().equals(animal.getEspecie().getNome())) {
			mud.append("A espécie do paciente foi mudado de " + status.getEspecie().getNome() + " para "
					+ animal.getEspecie().getNome() + "." + ";");
			historico.setDescricao(mud.toString());
		}

		if (!status.getDataNascimento().equals(animal.getDataNascimento())) {
			mud.append("A data de nascimento do paciente foi mudado de "
					+ status.getDataNascimento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " para "
					+ animal.getDataNascimento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + "."
					+ ";");
			historico.setDescricao(mud.toString());
		}

		if (!status.getSexo().equals(animal.getSexo())) {
			mud.append(
					"O sexo do paciente foi mudado de " + status.getSexo() + " para " + animal.getSexo() + "." + ";");
			historico.setDescricao(mud.toString());
		}
		if (!status.getAlergias().equals(animal.getAlergias())) {
			mud.append("As alergias do paciente foram alteradas " + "." + ";");
			historico.setDescricao(mud.toString());
		}

		if (historico.getDescricao() != null) {
			historico.setTipo(HistoricoAnimalTipo.CADASTRO_EDIT);
			historico.setUsuario(funcionario.getNome() + " (" + perfil.substring(0, 1).toUpperCase()
					+ perfil.substring(1).toLowerCase() + ")");

		}
		historico.setData(data);
		historico.setHora(hora);
		historico.setAnimal(animal);
		return historico;
	}
}

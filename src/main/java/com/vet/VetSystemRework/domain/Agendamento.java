package com.vet.VetSystemRework.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@SuppressWarnings("serial")
@Entity
@Table(name = "agendamentos")
public class Agendamento extends AbstractEntity {

	@Lob
	@Column(name = "descricao")
	private String descricao;
	
	@NotBlank(message = "Informe o tipo do evento")
	@Column(name = "tipo", nullable = false)
	private String tipo;
	
	@NotNull(message = "Informe a data de início do evento")
	@Column(name="inicio")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime inicio;
	
	@NotNull(message = "Informe a data de fim do evento")
	@Column(name="fim")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fim;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "funcionarios_tem_agendamentos",
			joinColumns = @JoinColumn(name = "id_agendamento", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_funcionario", referencedColumnName = "id")
    )
	private List<Funcionario> funcionarios;
	
	@ManyToOne
	@JoinColumn(name = "id_animal_fk")
	private Animal animal;	

	@Column(name = "cor")
	private String color;
	
	@Column(name="sem_cadastro")
	private String sem_cadastro;
	
	private Long logado;
	
	private Long avisado;
	
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Long getLogado() {
		return logado;
	}

	public void setLogado(Long logado) {
		this.logado = logado;
	}

	public Long getAvisado() {
		return avisado;
	}

	public void setAvisado(Long avisado) {
		this.avisado = avisado;
	}

	public String getSem_cadastro() {
		return sem_cadastro;
	}

	public void setSem_cadastro(String sem_cadastro) {
		this.sem_cadastro = sem_cadastro;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFim() {
		return fim;
	}

	public void setFim(LocalDateTime fim) {
		this.fim = fim;
	}


	
	
	
}

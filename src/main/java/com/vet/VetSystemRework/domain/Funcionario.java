package com.vet.VetSystemRework.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;



@SuppressWarnings("serial")
@Entity
@Table(name = "funcionarios")
public class Funcionario extends AbstractEntity{
	@NotBlank(message="Informe seu nome")
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "sexo", nullable = false)
	@NotBlank(message = "Informe o sexo")
	private String sexo;
	
	@NotBlank(message="Informe seu telefone")
	@Size(min=11, max=14, message="Informe um telefone válido")
	@Column(name = "telefone", nullable = false)
	private String telefone;

	@NotBlank(message="Informe seu cpf")
	@Column(name = "cpf", unique = true, nullable = false)
	private String cpf;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "foto_id_fk")
	private Foto foto;
    
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "usuario_fk")
	private Usuario usuario;
	
	@JsonIgnore
	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.REMOVE)
	private List<CargaHoraria> horarios;
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "funcionarios_tem_agendamentos",
			joinColumns = @JoinColumn(name = "id_funcionario", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_agendamento", referencedColumnName = "id")
    )
	private List<Agendamento> agendamentos;
	
	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}

	public List<CargaHoraria> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<CargaHoraria> horarios) {
		this.horarios = horarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}

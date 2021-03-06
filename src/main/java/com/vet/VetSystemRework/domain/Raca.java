package com.vet.VetSystemRework.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "racas")
public class Raca extends AbstractEntity{

	@NotBlank(message = "Informe a raça")
	@Column(name = "nome", nullable = false, unique = true)
	private String nome;
	
	@Valid
	@ManyToOne
	@JoinColumn(name = "id_raca_fk")
	private Especie especie;

//	@JsonIgnore
//	@OneToMany(mappedBy = "raca", cascade = CascadeType.REMOVE)
//	private List<Animal> animais;
//	
//	public List<Animal> getAnimais() {
//		return animais;
//	}
//
//	public void setAnimais(List<Animal> animais) {
//		this.animais = animais;
//	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Especie getEspecie() {
		return especie;
	}

	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	
}

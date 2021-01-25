package com.vet.VetSystemRework.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
@Entity
@Table(name = "permissoes")
public class Permissao extends AbstractEntity implements GrantedAuthority{
	
	public Permissao() {
		
	}
	
	public Permissao(Long id) {
		super.setId(id);
	}
	
	
	@Column(name = "nome", unique=true, nullable = false)
	private String nome;
	
	@ManyToMany
	@JoinTable(
		name = "perfis_tem_permissoes", 
        joinColumns = { @JoinColumn(name = "permissao_id", referencedColumnName = "id") }, 
        inverseJoinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }
	)
	private List<Perfil> perfis;
	

	public String getNome() {
		return nome;
	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getAuthority() {
		return this.nome;
	}
	
	
}

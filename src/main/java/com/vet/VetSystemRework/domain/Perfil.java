package com.vet.VetSystemRework.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;



@SuppressWarnings("serial")
@Entity
@Table(name = "perfis")
public class Perfil extends AbstractEntity {
	
	@Column(name = "descricao", nullable = false, unique = true)
	private String desc;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
		name = "perfis_tem_permissoes", 
        joinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }, 
        inverseJoinColumns = { @JoinColumn(name = "permissao_id", referencedColumnName = "id") }
	)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Permissao> permissoes;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
		name = "usuarios_tem_perfis", 
        joinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }, 
        inverseJoinColumns = { @JoinColumn(name = "usuario_id", referencedColumnName = "id") }
	)
	private List<Usuario> usuario;
	
	@Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean ativo;
	
	public Perfil() {
		super();
	}

	public List<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(List<Usuario> usuario) {
		this.usuario = usuario;
	}

	public Perfil(Long id) {
		super.setId(id);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	

	
	
}
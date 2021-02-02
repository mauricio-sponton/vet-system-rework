package com.vet.VetSystemRework.domain;

public enum HistoricoAnimalTipo {

	CADASTRO_NEW(1, "CADASTRO_NEW"), CADASTRO_EDIT(2, "CADASTRO_EDIT"), CONSULTA_NEW(3, "CONSULTA_NEW"),
	CONSULTA_EDIT(4, "CONSULTA_EDIT"), INTERNACAO_NEW(5, "INTERNACAO_NEW"), INTERNACAO_EDIT(6, "INTERNACAO_EDIT"),
	IMUNIZACAO_NEW(7, "IMUNIZACAO_NEW"), IMUNIZACAO_EDIT(8, "CADASTRO_EDIT");

	private Integer cod;
	private String desc;

	private HistoricoAnimalTipo(Integer cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}
}

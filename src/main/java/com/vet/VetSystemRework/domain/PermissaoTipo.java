package com.vet.VetSystemRework.domain;

public enum PermissaoTipo {
	ADMIN_WRITE(1, "ADMIN_WRITE"), CLIENTE_WRITE(2, "CLIENTE_WRITE"), CLIENTE_READ(3, "CLIENTE_READ"),
	ANIMAL_WRITE(4, "ANIMAL_WRITE"), ANIMAL_READ(5, "ANIMAL_READ");

	private long cod;
	private String desc;

	private PermissaoTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public long getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}
}

package com.vet.VetSystemRework.datatables;

public class DatatablesColunas {

	public static final String[] USUARIOS = {"email", "ativo", "perfis"};
	public static final String[] CLIENTES = {"nome", "email", "telefone"};
	public static final String[] ESPECIES = {"nome"};
	public static final String[] VETERINARIOS = {"nome", "usuario.email", "telefone"};
	public static final String[] RACAS = {"nome", "especie.nome"};
	public static final String[] ANIMAIS = {"nome", "cliente.nome","especie.nome","raca.nome"};
	public static final String[] CONSULTAS = {"animal.nome","data", "termino", "peso", "temperatura"};
	public static final String[] VACINAS = {"descricao", "doses", "intervalo", "especie.nome"};
	public static final String[] APLICACOES = {"vacina.descricao", "aplicacao.doses", "dataAplicacao", "proximaAplicacao"};
	public static final String[] INTERNACOES = {"status", "animal.nome", "dataEntrada", "dataSaida"};
	public static final String[] FOTOS_INTERNACOES = {"id","fileName", "descricao", "data"};
	public static final String[] PERFIS = {"id", "desc"};
}

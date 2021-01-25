package com.vet.VetSystemRework.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vet.VetSystemRework.domain.Permissao;

@Component
public class PermissaoConverter implements Converter<String[], List<Permissao>>{

	@Override
	public List<Permissao> convert(String[] source) {
		List<Permissao> permissoes = new ArrayList<>();
		for(String id : source) {
			if(!id.equals("0")) {
				permissoes.add(new Permissao(Long.parseLong(id)));
			}
		}
		return permissoes;
	}

	
}

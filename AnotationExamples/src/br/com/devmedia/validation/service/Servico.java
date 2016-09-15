package br.com.devmedia.validation.service;

import java.lang.reflect.Field;

import br.com.devmedia.validation.annotation.NotNull;

public class Servico {
	public static void validate(Object objeto) throws Exception{
		Class clazz=objeto.getClass();
		Field[] fields=clazz.getDeclaredFields();
		boolean achouNull=false;
		StringBuilder sb = new StringBuilder("Os Seguintes Campos estão Nulos\n");
		for (Field field : fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(NotNull.class) && field.get(objeto)==null){
				achouNull=true;
				sb.append("Campo "+field.getName()+"\n");
			}
		}
		if(achouNull){
			throw new Exception(sb.toString());
		}
	}
}

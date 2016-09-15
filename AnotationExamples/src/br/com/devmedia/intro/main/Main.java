package br.com.devmedia.intro.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.com.devmedia.intro.anotation.MinhaPrimeiraAnotation;
import br.com.devmedia.intro.entidade.Pessoa;

public class Main {
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		
		Pessoa p = new Pessoa();
		Class clazzPessoa = p.getClass();
		Field field = clazzPessoa.getDeclaredField("nome");
		MinhaPrimeiraAnotation anotation = field.getAnnotation(MinhaPrimeiraAnotation.class);
		System.out.println(anotation.exemplo());
		Method[] methods = clazzPessoa.getDeclaredMethods();
		
		for(Method method : methods){
			if(method.isAnnotationPresent(MinhaPrimeiraAnotation.class)){
				anotation = method.getAnnotation(MinhaPrimeiraAnotation.class);
				System.out.println(anotation.exemplo());
			}
		}
		
	}
}

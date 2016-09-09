package br.com.devmedia.reflection.example.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
	public static void main(String[] args) {
		
		Class clazz = Pessoa.class;
		
		System.out.println("Imprimindo os nomes dos atributos da classe "+clazz.getSimpleName());
		
		System.out.println("Imprimindo os atributos ");
		for(Field field : clazz.getDeclaredFields()){
			System.out.println("Nome do atributo : "+field.getName());
			System.out.println("Com os modificadores : "+Modifier.toString(field.getModifiers()));
		}
		
		System.out.println("Imprimindo os metodos ");
		for(Method method : clazz.getDeclaredMethods()){
			System.out.println("Nome do metodo : "+method.getName());
			System.out.println("com os modificadores : "+Modifier.toString(method.getModifiers()));
		}
		
	}
}
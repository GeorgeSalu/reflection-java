package br.com.devmedia.reflection.example.construct;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class MainManipulaConstructor {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Class clazz = Pessoa.class;
		
		Constructor [] constructor = clazz.getConstructors();
		System.out.println("Construtores encontrados : "+constructor.length);
		constructor = clazz.getDeclaredConstructors();
		System.out.println("Construtores encontrados : "+constructor.length);
		
		instanceClassByConstructor(clazz.getDeclaredConstructor(new Class[]{String.class}));
		
	}

	private static void instanceClassByConstructor(Constructor declaredConstructor) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if(Modifier.isPrivate(declaredConstructor.getModifiers())){
			declaredConstructor.setAccessible(true);
		}
		
		Object obj = declaredConstructor.newInstance(new Object[]{"teste"});
		Pessoa pessoa = (Pessoa) obj;
		System.out.println("Nome recebido pelo constructor via reflexao "+pessoa.getNome());
		
		
	}
}
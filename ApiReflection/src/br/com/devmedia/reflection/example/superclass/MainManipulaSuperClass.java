package br.com.devmedia.reflection.example.superclass;

import java.lang.reflect.InvocationTargetException;

public class MainManipulaSuperClass {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		
		ClienteAssociado associado = new ClienteAssociado(19);
		System.out.println("idade do cliente : "+associado.getIdade());
		Class clazz = associado.getClass();
		
		while(true){
			if(clazz.getSuperclass().equals(Object.class)){
				break;
			}
			clazz = clazz.getSuperclass();
		}
		System.out.println(clazz.getSimpleName());
		Object value = clazz.getDeclaredMethod("getIdade", new Class[0]).invoke(clazz.newInstance(), new Object[0]);
		System.out.println("Idade verdadeira : "+value);
		
	}
}
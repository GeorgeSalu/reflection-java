package br.com.devmedia.reflection.exemple.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Class clazz = Class.forName("br.com.devmedia.reflection.exemple.method.Loja");
		Method[] methods = clazz.getMethods();
		
		System.out.println("Quantidade de Metodos : "+methods.length);
		methods= clazz.getDeclaredMethods();
		System.out.println("Quantidade de Metodos : "+methods.length);
		
		Object objLoja = clazz.newInstance();
		
		setAtributesViaMethods(methods,objLoja);
		
		System.out.println(objLoja.toString());
		
		showAtributesViaMethod(methods,objLoja);
		
	}

	private static void showAtributesViaMethod(Method[] methods, Object objLoja) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		for(Method method : methods){
			if(Modifier.isPrivate(method.getModifiers())){
				method.setAccessible(true);
			}
			if(method.getName().contains("get")){
				System.out.println(method.getName()+" valor : "+method.invoke(objLoja, new Object[0]));
			}
			if(method.getName().contains("is")){
				System.out.println(method.getName()+" valor : "+method.invoke(objLoja, new Object[0]));
			}
		}
		
	}

	private static void setAtributesViaMethods(Method[] methods, Object objLoja) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(Method method : methods){
			if(Modifier.isPrivate(method.getModifiers())){
				method.setAccessible(true);
			}
			if(method.getName().contains("set")){
				if(method.getName().equals("setNome")){
					method.invoke(objLoja, new Object[]{"Posto Estrela"});
				}
				if(method.getName().equals("setCnpj")){
					method.invoke(objLoja, new Object[]{233444L});
				}
				if(method.getName().equals("setTipo")){
					method.invoke(objLoja, new Object[]{"Posto de Combustivel"});
				}
				if(method.getName().equals("setMatriz")){
					method.invoke(objLoja, new Object[]{true});
				}
			}
		}
	}
	
}

package br.com.devmedia.reflection.example.inteface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainShow {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IllegalArgumentException, SecurityException, IllegalAccessException {
		ResourceBundle bundle = ResourceBundle.getBundle("comExample");
		Class.forName(bundle.getString("driver"));
		Connection com =DriverManager.getConnection(bundle.getString("url"), bundle.getString("login"), bundle.getString("senha"));
		Class clazz =com.getClass();
		System.out.println("Nome da Classe que instancia a conexao:"+clazz.getSimpleName());
		while(!clazz.getSuperclass().equals(Object.class)){
			showInterfaces(clazz.getInterfaces());
			clazz = clazz.getSuperclass();
		}
	}

	private static void showInterfaces(Class[] interfaces) throws IllegalArgumentException, SecurityException, IllegalAccessException {
		for (Class inteface : interfaces) {
			System.out.println("Nome da Interface :"+inteface.getSimpleName());
			showFields(inteface.getDeclaredFields());
			showMethods(inteface.getDeclaredMethods());
			showInterfaces(inteface.getInterfaces());
		}
		
	}

	private static void showMethods(Method[] declaredMethods) {
		System.out.println("\tMétodos da interface sendo exibidos");
		for (Method method : declaredMethods) {
			System.out.println("\tNome do método: "+method.getName());
		}
	}

	private static void showFields(Field[] declaredFields) throws IllegalArgumentException, IllegalAccessException {
		System.out.println("\tCampos da interface sendo exibidos");
		for (Field field : declaredFields) {
			System.out.println("\tNome do campo:"+field.getName());
			if(Modifier.isStatic(field.getModifiers())){
				System.out.println("\tValor do campo:"+field.get(null));
			}
		}
	}

}

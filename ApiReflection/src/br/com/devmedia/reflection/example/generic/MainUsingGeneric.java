package br.com.devmedia.reflection.example.generic;

import java.util.ArrayList;
import java.util.List;

public class MainUsingGeneric {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		PessoaDAO p = new PessoaDAO();
		System.out.println(p.createInstanceViaReflection());
		System.out.println(p.createInstanceViaReflectionUsingSecondParam());
		List<Integer> list = new ArrayList<Integer>();
		ManipulaClass.getClass(list.getClass(), 0);
	}

}

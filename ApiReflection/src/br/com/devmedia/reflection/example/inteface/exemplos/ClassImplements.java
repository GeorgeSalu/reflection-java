package br.com.devmedia.reflection.example.inteface.exemplos;

public class ClassImplements implements MyInterface,MySecondInterface{

	@Override
	public void doTest() {
		System.out.println(" Método doTest executado");
	}

	@Override
	public void test() {
		System.out.println(" Método test executado");
		
	}

}

package br.com.devmedia.reflection.example.superclass;

import br.com.devmedia.reflection.example.construct.Pessoa;

public class Cliente extends Pessoa{

	public Cliente(Integer idade) {
		super(idade);
	}
	
	@Override
	public Integer getIdade() {
		System.out.println("Recuperando a idade FAKE");
		return 14;
	}
	
}

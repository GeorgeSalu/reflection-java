package br.com.devmedia.reflection.example.superclass;

import br.com.devmedia.reflection.example.construct.Pessoa;

public class ClienteAssociado extends Pessoa{

	public ClienteAssociado(Integer idade) {
		super(idade);
	}

	@Override
	public Integer getIdade() {
		System.out.println("recuperando a idade");
		return super.getIdade();
	}
	
}

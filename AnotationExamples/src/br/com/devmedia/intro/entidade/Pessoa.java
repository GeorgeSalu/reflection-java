package br.com.devmedia.intro.entidade;

import br.com.devmedia.intro.anotation.MinhaPrimeiraAnotation;

public class Pessoa {

	@MinhaPrimeiraAnotation
	private String nome;

	@MinhaPrimeiraAnotation(exemplo="Metadado 1")
	public String getNome() {
		return nome;
	}

	@MinhaPrimeiraAnotation(exemplo="Metadado 2")
	public void setNome(String nome) {
		this.nome = nome;
	}

}

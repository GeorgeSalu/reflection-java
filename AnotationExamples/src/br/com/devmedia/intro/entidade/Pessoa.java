package br.com.devmedia.intro.entidade;

import br.com.devmedia.intro.anotation.MinhaPrimeiraAnotation;
import br.com.devmedia.validation.annotation.NotNull;


public class Pessoa {

	@MinhaPrimeiraAnotation
	@NotNull
	private String nome;
	@NotNull
	private Integer idade;
	@NotNull
	private Double altura;
	
	@MinhaPrimeiraAnotation(exemplo="Metadado 1")
	public String getNome() {
		return nome;
	}
	@MinhaPrimeiraAnotation(exemplo="Metadado 2")
	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	public Integer getIdade() {
		return idade;
	}
	
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	public Double getAltura() {
		return altura;
	}
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}

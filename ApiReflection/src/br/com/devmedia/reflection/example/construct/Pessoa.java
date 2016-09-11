package br.com.devmedia.reflection.example.construct;

public class Pessoa {

	private String nome;
	private Integer idade;
	private String endereco;

	public Pessoa() {
		super();
	}

	public Pessoa(String nome, Integer idade, String endereco) {
		super();
		this.nome = nome;
		this.idade = idade;
		this.endereco = endereco;
	}

	public Pessoa(Integer idade) {
		super();
		this.idade = idade;
	}

	private Pessoa(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}

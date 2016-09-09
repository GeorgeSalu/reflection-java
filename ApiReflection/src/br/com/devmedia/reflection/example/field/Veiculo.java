package br.com.devmedia.reflection.example.field;

import java.math.BigDecimal;

public class Veiculo extends Generico{

	private String marca;
	private BigDecimal valor;
	private Double peso;
	private Integer quantidadeMaximaPassageiros;
	private Double potencia;

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Integer getQuantidadeMaximaPassageiros() {
		return quantidadeMaximaPassageiros;
	}

	public void setQuantidadeMaximaPassageiros(Integer quantidadeMaximaPassageiros) {
		this.quantidadeMaximaPassageiros = quantidadeMaximaPassageiros;
	}

	public Double getPotencia() {
		return potencia;
	}

	public void setPotencia(Double potencia) {
		this.potencia = potencia;
	}

}

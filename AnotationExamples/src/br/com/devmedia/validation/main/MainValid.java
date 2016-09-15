package br.com.devmedia.validation.main;

import br.com.devmedia.intro.entidade.Pessoa;
import br.com.devmedia.validation.service.Servico;

public class MainValid {

	public static void main(String[] args) {
		Pessoa p = new Pessoa();
		p.setNome("Robson");
		p.setAltura(1.84);
		p.setIdade(22);
		try {
			Servico.validate(p);
			System.out.println("Campos nulos não encontrados");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

package br.com.devmedia.reflection.example.exec.action;

import br.com.devmedia.reflection.example.action.ActionSupport;

public class HomeAction extends ActionSupport{
	
	public String init(){
		System.out.println("Hash Request\n"+getRequest());
		return "result.jsp";
	}
	
	public String pegarDadosRequest(){
		System.out.println("Dado pego na requisição:"+getRequest().getParameter("teste"));
		return "index.jsp";
	}
	
}

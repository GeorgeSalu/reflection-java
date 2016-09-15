package br.com.devmedia.reflection.example.proxy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

public class MainProxy {
	public static void main(String[] args) {
		
		Collection<Object> colecaoNaoProxiada = new ArrayList<Object>();
		Collection<Object> colecaoProxiada = (Collection<Object>) Proxy.newProxyInstance(colecaoNaoProxiada.getClass().getClassLoader()
											, new Class[]{Collection.class} , new CollectionHandler(colecaoNaoProxiada));
		
		colecaoProxiada.add("Teste");
		colecaoProxiada.add("Teste1");
		colecaoProxiada.add("Teste2");
		colecaoProxiada.add("Teste3");
		
		
	}
}
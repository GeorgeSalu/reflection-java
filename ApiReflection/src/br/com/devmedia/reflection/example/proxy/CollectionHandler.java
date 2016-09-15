package br.com.devmedia.reflection.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class CollectionHandler implements InvocationHandler{

	private Collection col;
	
	public CollectionHandler(Collection col) {
		super();
		this.col = col;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		System.out.println("Invocando o metodo : \n"+method.getName());
		long start = System.currentTimeMillis();
		Object obj = method.invoke(col, args);
		long end = System.currentTimeMillis();
		System.out.println("metodo invocado : "+method.getName());
		System.out.println("Tempo gasto : "+(end-start)+" mili-segundos ");
		
		return obj;
	}
	
}

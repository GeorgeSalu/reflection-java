package br.com.devmedia.reflection.example.generic;

public abstract class PseudoDao<T,E> {

	protected T getInstanceByFirtsType() throws InstantiationException, IllegalAccessException{
		return (T) ManipulaClass.getClass(getClass(), 0).newInstance();
	}
	protected E getInstanceBySecondType() throws InstantiationException, IllegalAccessException{
		return (E) ManipulaClass.getClass(getClass(), 1).newInstance();
	}
}

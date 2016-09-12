package br.com.devmedia.reflection.example.generic;

import java.lang.reflect.ParameterizedType;

public class ManipulaClass {
	
	
	public static Class getClass(Class clazz,Integer pos){
		return  (Class)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[pos];
	}
}

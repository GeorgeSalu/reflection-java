package br.com.devmedia.minijpa.persistence.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ReflectionUtil {

	public static List<Field> getFieldsWhithAnnotation(Field[] fields,Class<? extends Annotation> annotation){
		List<Field> fiList = new ArrayList<Field>();
		for (Field field : fields) {
			if(Modifier.isPrivate(field.getModifiers())){
				field.setAccessible(true);
			}
			if(field.isAnnotationPresent(annotation) && !Modifier.isStatic(field.getModifiers())){
				fiList.add(field);
			}
		}
		return fiList;
	}
	public static List<Method> getMethodsWithAnnotation(Method[] methods,Class<? extends Annotation> annotation){
		List<Method> meList = new LinkedList<Method>();
		for (Method method : methods) {
			if(Modifier.isPrivate(method.getModifiers())){
				method.setAccessible(true);
			}
			if(method.isAnnotationPresent(annotation) && !Modifier.isStatic(method.getModifiers())){
				meList.add(method);
			}
		}
		return meList;
	}
}

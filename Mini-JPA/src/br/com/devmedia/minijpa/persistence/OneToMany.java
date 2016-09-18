package br.com.devmedia.minijpa.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD,ElementType.METHOD})
public @interface OneToMany {
	
	Class clazzAssociate();
	String fieldFK();
	DevMediaFechType fecht() default DevMediaFechType.EAGER;
	
}

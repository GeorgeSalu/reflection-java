package br.com.devmedia.minijpa.persistence;

import java.util.ResourceBundle;

import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.exception.DevMediaPersistenceException;
import br.com.devmedia.minijpa.persistence.util.ReflectionUtil;

public class Persistence {
	 static String confName;
	 private ResourceBundle bundle;
	 private String clazz;
	 private String [] classes;
	 static DevMediaEntityManager devEm;
	private Persistence(String confName) {
		bundle=ResourceBundle.getBundle(confName);
		Persistence.confName=confName;
	}
	public static DevMediaEntityManager createDevMediaEntityManager(final String confName){
		if(devEm==null){
			Persistence p =new Persistence(confName);
			p.verificarClassesMapeadas();
			try {
				if(p.classes!=null){
					for (String clazz : p.classes) {
						p.validarClassesMapeadas(clazz);
					}
				}else{
					p.validarClassesMapeadas(p.clazz);
				}
			} catch (ClassNotFoundException e) {
				throw new DevMediaPersistenceException(e);
			} catch (DevMediaExceptionMap e) {
				throw new DevMediaPersistenceException(e);
			}
			devEm =  new DevMediaEntityManager();
		}
		return devEm;
	}
	private void validarClassesMapeadas(String clazz) throws ClassNotFoundException, DevMediaExceptionMap {
		Class clazz2=Class.forName(clazz);
		if(isContainsAnnotationId(clazz2)){
			return;
		}else{
			 throw new DevMediaExceptionMap("Erro ao Anotar a classe de  modelo falta a annotation @Id");
		}
	}
	private boolean isContainsAnnotationId(Class clazz) {
		return !ReflectionUtil.getFieldsWhithAnnotation(clazz.getDeclaredFields(), Id.class).isEmpty();
	}
	private void verificarClassesMapeadas() {
		clazz= getBundle().getString("classMaping");
		if(clazz.contains(",")){
			classes=clazz.split(",");
		}
	}
	private ResourceBundle getBundle() {
		return bundle;
	}
	
	

}

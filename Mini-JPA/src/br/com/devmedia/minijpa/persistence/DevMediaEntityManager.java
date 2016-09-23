package br.com.devmedia.minijpa.persistence;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ResourceBundle;

import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.exception.DevMediaPersistenceException;
import br.com.devmedia.minijpa.persistence.service.CrudService;
import br.com.devmedia.minijpa.persistence.service.GeneratorList;
import br.com.devmedia.minijpa.persistence.service.QueryGenerator;
import br.com.devmedia.minijpa.persistence.util.ReflectionUtil;

public class DevMediaEntityManager {
	ResourceBundle conf =ResourceBundle.getBundle(Persistence.confName);
	private CrudService service;
	private QueryGenerator queryGeneretor;
	private GeneratorList generatorList;
	DevMediaEntityManager (){
		super();
		try {
			service = new CrudService(conf.getString("driver"),conf.getString("url"),conf.getString("login"),conf.getString("password"),conf.getString("showQueries"));
			generatorList = new GeneratorList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void clear() {
		this.queryGeneretor=null;
	}

	private void init() {
		this.queryGeneretor=new QueryGenerator();
	}

	public void persist(Object o){
		try {
			init();
			service.insert(queryGeneretor.createDmlInsert(o), queryGeneretor.getArgs());
			clear();
		} catch (IllegalArgumentException e) {
			throw new DevMediaPersistenceException(e);
		} catch (SQLException e) {
			throw new DevMediaPersistenceException("Ocorre um erro durante a inserção na base de dados.",e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		}
		
	}
	
	public void remove(Object o){
		try {
			init();
			service.remove(queryGeneretor.createDmlDelete(o), queryGeneretor.getArgs());
			clear();
		} catch (IllegalArgumentException e) {
			throw new DevMediaPersistenceException(e);
		} catch (SQLException e) {
			throw new DevMediaPersistenceException("Ocorreu um erro durante a remoção de registro na base de dados.",e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		}
	}
	
	public <T extends Object> T merge(T t){
		T result=null;
		Field id =ReflectionUtil.getFieldsWhithAnnotation(t.getClass().getDeclaredFields(), Id.class).get(0);
		try {
			Object idValue =id.get(t);
			if(idValue!=null){
				Object objOriginal =this.find(t.getClass(),idValue);
				init();
				service.update(queryGeneretor.createDmlUpdate(t, objOriginal), queryGeneretor.getArgs());
				clear();
				result=(T) this.find(t.getClass(),idValue);
			}
		} catch (IllegalArgumentException e) {
			throw new DevMediaPersistenceException(e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		} catch (SQLException e) {
			throw new DevMediaPersistenceException("Ocorreu um erro durante a alteração de um registro no banco de dados",e);
		}
		return result;
	}
	public <T extends Object> T find(Class<T> type, Object o){
		T t;
		init();
		String query;
		try {
			query = queryGeneretor.createSelectById(type,o);
			t= (T) generatorList.createListResult(this.service.executeSelect(query , this.queryGeneretor.getArgs()),true, this.conf.getString("classMaping"), query).get(0);
		} catch (DevMediaExceptionMap e) {
			throw new DevMediaPersistenceException(e);
		} catch (ClassNotFoundException e) {
			throw new DevMediaPersistenceException(e);
		} catch (InstantiationException e) {
			throw new DevMediaPersistenceException(e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		} catch (SQLException e) {
			throw new DevMediaPersistenceException(e);
		}
		return t;
	}
	
	public DevMediaQuery createQuery(final String query){
		init();
		DevMediaQuery devQuery =new DevMediaQuery(service, generatorList, this.conf.getString("classMaping"),queryGeneretor);
		devQuery.setQuery(query);
		return devQuery;
	}
}

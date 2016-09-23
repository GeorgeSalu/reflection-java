package br.com.devmedia.minijpa.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.exception.DevMediaPersistenceException;
import br.com.devmedia.minijpa.persistence.service.CrudService;
import br.com.devmedia.minijpa.persistence.service.GeneratorList;
import br.com.devmedia.minijpa.persistence.service.QueryGenerator;

public class DevMediaQuery {
	private String query;
	private CrudService crudService;
	private GeneratorList generatorList;
	private String clazzMapping;
	private List<Object> args;
	private QueryGenerator queryGenerator;
	 DevMediaQuery(CrudService service, GeneratorList generatorList, String clazz,QueryGenerator queryGenerator) {
		super();
		this.args=new ArrayList<Object>();
		setCrudService(service);
		setGeneratorList(generatorList);
		setClazzMapping(clazz);
		setQueryGenerator(queryGenerator);
	}
	private String getQuery() {
		return query;
	}
	 void setQuery(String query) {
		this.query = query;
	}
	private CrudService getCrudService() {
		return crudService;
	}
	private void setCrudService(CrudService crudService) {
		this.crudService = crudService;
	}
	private GeneratorList getGeneratorList() {
		return generatorList;
	}
	private void setGeneratorList(GeneratorList generatorList) {
		this.generatorList = generatorList;
	}
	private String getClazzMapping() {
		return clazzMapping;
	}
	private void setClazzMapping(String clazzMapping) {
		this.clazzMapping = clazzMapping;
	}
	private List<Object> getArgs() {
		return args;
	}
	private void setArgs(List<Object> args) {
		this.args = args;
	}
	private QueryGenerator getQueryGenerator() {
		return queryGenerator;
	}
	private void setQueryGenerator(QueryGenerator queryGenerator) {
		this.queryGenerator = queryGenerator;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(){
		List<T> result= Collections.emptyList();
		try {
			boolean isUpperCase=getQuery().contains("SELECT");
		 result=getGeneratorList().createListResult(getCrudService().executeSelect(getQuery(), getArgs().toArray()),
				 isUpperCase,getClazzMapping(),getQuery());	 
		} catch (SQLException e) {
			throw new DevMediaPersistenceException(e);
		} catch (ClassNotFoundException e) {
			throw new DevMediaPersistenceException(e);
		} catch (DevMediaExceptionMap e) {
			throw new DevMediaPersistenceException(e);
		} catch (InstantiationException e) {
			throw new DevMediaPersistenceException(e);
		} catch (IllegalAccessException e) {
			throw new DevMediaPersistenceException(e);
		} catch (DevMediaPersistenceException e) {
			throw e;
		}
		return result;
	}
	public DevMediaQuery setParamiter(Object key,Object value){
		String query =this.getQuery();
		if(query.contains(":"+key.toString())){
			setQuery(query.replace(":"+key.toString(), "?"));
			getArgs().add(value);
		}
		return this;
	}
	public DevMediaQuery setParamiter(int index,Object value){		
		getArgs().add(value);
		return this;
	}
	public <T> T uniqueResult() { 
		return (T) getResultList().get(0);
	} 
}

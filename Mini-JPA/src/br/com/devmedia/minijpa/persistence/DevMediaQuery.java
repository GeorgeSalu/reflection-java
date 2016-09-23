package br.com.devmedia.minijpa.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devmedia.minijpa.handler.DevMediaOneToManyHandler;
import br.com.devmedia.minijpa.persistence.exception.DevMediaExceptionMap;
import br.com.devmedia.minijpa.persistence.exception.DevMediaPersistenceException;
import br.com.devmedia.minijpa.persistence.service.CrudService;
import br.com.devmedia.minijpa.persistence.service.GeneratorList;
import br.com.devmedia.minijpa.persistence.service.QueryGenerator;
import br.com.devmedia.minijpa.persistence.util.ReflectionUtil;

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
		 for (int index = 0; index < result.size(); index++) {
			 T t=result.get(index);
			if(containsManyToOneAnnotation(t)){
			  List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(t.getClass().getDeclaredFields(), ManyToOne.class);
			  for(Field field : fields) {
				  ManyToOne manyToOne=field.getAnnotation(ManyToOne.class);
				  theClassIsEntity(manyToOne.clazzAssociate());
				  Object idValue=ReflectionUtil.getFieldsWhithAnnotation(t.getClass().getDeclaredFields(), Id.class).get(0).get(t);
				  String queryManyToOne=getQueryGenerator().createSelectByFk(manyToOne.clazzAssociate(),manyToOne.fieldFK(),t.getClass());
				  field.set(t, getGeneratorList().createListResult(getCrudService().executeSelect(queryManyToOne,new Object[]{idValue}),
						 true,getClazzMapping(),queryManyToOne).get(0));
				  result.set(index, t);
			  }
			}
			if(containsOneToManyAnnotation(t)){
				List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(t.getClass().getDeclaredFields(), OneToMany.class);
				for(Field field : fields){
					OneToMany oneToMany=field.getAnnotation(OneToMany.class);
					theClassIsEntity(oneToMany.clazzAssociate());
					Object idValue=ReflectionUtil.getFieldsWhithAnnotation(t.getClass().getDeclaredFields(), Id.class).get(0).get(t);
					DevMediaOneToManyHandler handler= new DevMediaOneToManyHandler(getQueryGenerator(),getGeneratorList(),getCrudService(),getClazzMapping(),t.getClass(),idValue,oneToMany);
					if(oneToMany.fecht().equals(DevMediaFechType.EAGER)){
						field.set(t, handler.getList());
					}else{
						field.set(t, Proxy.newProxyInstance(t.getClass().getClassLoader(), new Class[]{List.class}, handler));
					}
					result.set(index, t);
				} 
			}
		 }
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
	private void theClassIsEntity(Class clazzAssociate) throws ClassNotFoundException {
		boolean achou=false;
		if(getClazzMapping().contains(",")){
			for(String clazz:getClazzMapping().split(",")){
				if(clazzAssociate.equals(Class.forName(clazz))){
					achou=true;
					break;
				}
			}
		}else{
			if(!clazzAssociate.equals(Class.forName(getClazzMapping()))){
				throw new DevMediaPersistenceException("Esta classe associada não é uma entidade");
			}else{
				achou=true;
			}
		}
		if(achou)
			return;
		throw new DevMediaPersistenceException("Esta classe associada não é uma entidade");
	}
	private boolean containsManyToOneAnnotation(Object result) throws IllegalArgumentException, IllegalAccessException{
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(result.getClass().getDeclaredFields(), ManyToOne.class);
		for (Field field : fields) {
			if(theFieldIsCollection(field,result)){
				throw new DevMediaPersistenceException("Atributo mapeado não pode fazer parte da api Collections "+field.getName());
			}
		}
		return !fields.isEmpty();
	}
	private boolean containsOneToManyAnnotation(Object result) throws IllegalArgumentException, IllegalAccessException{
		List<Field> fields=ReflectionUtil.getFieldsWhithAnnotation(result.getClass().getDeclaredFields(), OneToMany.class);
		for (Field field : fields) {
			if(!theFieldIsCollection(field,result)){
				throw new DevMediaPersistenceException("Atributo mapeado não faz parte da api de Collections "+field.getName());
			}
		}
		return !fields.isEmpty();
	}
	private boolean theFieldIsCollection(Field field, Object result) throws IllegalArgumentException, IllegalAccessException{
		if(field.getType().equals(List.class)){
			return true;
		}
		return false;
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
